package com.example.running.Service;

import com.example.running.Entity.Coordinate;
import com.example.running.Entity.RunningHistory;
import com.example.running.Entity.User;
import com.example.running.dto.RunningInfoRequest;;
import com.example.running.dto.RunningInfoResponse;
import com.example.running.repository.CoordinateRepository;
import com.example.running.repository.RunningHistoryRepository;
import com.example.running.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestProcessService {

    private final UserRepository userRepository;
    private final RunningHistoryRepository runningHistoryRepository;
    private final CoordinateRepository coordinateRepository;

    private final DtoConvertingService dtoConvertingService;

    @Transactional
    public void updateUserRunningHistory(Long userId, RunningInfoRequest request) {

        // 요청된 사용자 불러오기
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }

        User user = userOptional.get();

        // 새로운 달리기 기록 생성 (달리기 좌표 리스트는 초기화 X)
        RunningHistory runningHistory = RunningHistory.builder()
                .user(user)
                .runningStartedAt(request.getRunningStartedAt())
                .runningEndAt(request.getRunningEndAt())
                .runningCoordinateList(new ArrayList<>())
                .build();

        // 달리기 좌표 생성
        for(RunningInfoRequest.RunningPointInfo runningPoint : request.getRunningPointList()) {
            Coordinate coordinate = Coordinate.builder()
                    .latitude(runningPoint.getLatitude())
                    .longitude(runningPoint.getLongitude())
                    .height(runningPoint.getHeight())
                    .snapshotTime(runningPoint.getSnapshotTime())
                    .runningHistory(runningHistory)
                    .build();

            coordinateRepository.save(coordinate);  // 달리기 좌표 DB에 저장

            runningHistory.addCoordinate(coordinate);  // 달리기 기록의 좌표 리스트 초기화
        }

        runningHistoryRepository.save(runningHistory);  // 달리기 기록 DB에 저장

        // 요청된 사용자의 달리기 기록 update
        user.addRunningHistory(runningHistory);
        userRepository.save(user);
    }

    @Transactional
    public RunningInfoResponse selectUserRunningHistory(Long userId, Long runningId) {  // controller에 요청으로 userId, runningId 들어옴
        // 해당 유저의 특정 달리기 기록을 가공해서 불러오기
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {  // 존재하지 않는 사용자인 경우
            throw new IllegalArgumentException();
        }

        User user = userOptional.get();

        Optional<RunningHistory> runningHistoryOptional = runningHistoryRepository.findById(runningId);

        if(runningHistoryOptional.isEmpty()) {  // 존재하지 않는 달리기 기록인 경우
            throw new IllegalArgumentException();
        }

        RunningHistory runningHistory = runningHistoryOptional.get();

        if(!Objects.equals(user.getId(), runningHistory.getUser().getId())) {  // 요청으로 들어온 달리기 기록이 사용자의 것이 아닌 경우
            throw new IllegalArgumentException();
        }

        // ↑ 이 validation 과정 따로 검증 서비스 만들어서 책임 나눠야 하나?

        // Response Dto 필드 생성
        LocalDateTime runningStartedAt = dtoConvertingService.getRunningStartedAt(runningHistory);
        LocalDateTime runningEndAt = dtoConvertingService.getRunningEndAt(runningHistory);
        String totalDistance = dtoConvertingService.getTotalRunningDistance(runningHistory) + " km";

        Duration totalTimeTemporal = dtoConvertingService.getTotalRunningTime(runningHistory);
        String totalTime = totalTimeTemporal.toHours() + "H " + totalTimeTemporal.toMinutesPart() + "Min "
                + totalTimeTemporal.toSecondsPart() + "Sec";

        String averageVelocityPerKm = dtoConvertingService.getAvgVelocityPerKm(runningHistory) + " km/h";

        List<String> averageVelocitiesPerInterval = new ArrayList<>();
        for(Double averageVelocity : dtoConvertingService.getAvgVelocitiesPerInterval(runningHistory)) {
            averageVelocitiesPerInterval.add(averageVelocity + " km/h");
        }

        List<RunningInfoResponse.RunningPoint> coordinateListPerKm = new ArrayList<>();
        for(Coordinate coordinate : dtoConvertingService.getRunningCoordinatesPerKm(runningHistory)) {
            coordinateListPerKm.add(RunningInfoResponse.RunningPoint.builder()
                    .latitude(coordinate.getLatitude())
                    .longitude(coordinate.getLongitude())
                    .build()
            );
        }

        List<RunningInfoResponse.RunningPoint> allCoordinateList = new ArrayList<>();
        for(Coordinate coordinate : dtoConvertingService.getAllRunningCoordinates(runningHistory)) {
            coordinateListPerKm.add(RunningInfoResponse.RunningPoint.builder()
                    .latitude(coordinate.getLatitude())
                    .longitude(coordinate.getLongitude())
                    .build()
            );
        }

        return RunningInfoResponse.builder()
                .runningStartedAt(runningStartedAt)
                .runningEndAt(runningEndAt)
                .totalDistance(totalDistance)
                .totalTime(totalTime)
                .averageVelocityPerKm(averageVelocityPerKm)
                .averageVelocitiesPerInterval(averageVelocitiesPerInterval)
                .coordinateListPerKm(coordinateListPerKm)
                .allCoordinateList(allCoordinateList)
                .build();

    }
}
