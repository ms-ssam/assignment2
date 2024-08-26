package com.example.running.Service;

import com.example.running.Entity.Coordinate;
import com.example.running.Entity.RunningHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DtoConvertingService {

    private final DistanceService distanceService;

    // 달린 총 거리 구하기
    public Double getTotalRunningDistance(RunningHistory runningHistory) {

        List<Coordinate> coordinateList = runningHistory.getRunningCoordinateList();

        // snapshotTime 기준으로 정렬
        coordinateList = coordinateList.stream()
                .sorted(Comparator.comparing(Coordinate::getSnapshotTime))
                .toList();

        Coordinate operandCoordinate = coordinateList.get(0);
        double totalDistance = 0;

        for(Coordinate coordinate : coordinateList) {  // 두 지점 사이의 거리를 모두 합하기
            totalDistance += distanceService.getDistanceBetweenPoints(operandCoordinate, coordinate);
            operandCoordinate = coordinate;
        }

        return Math.round(totalDistance*100) / 100.0;
    }

    // 달린 총 시간 구하기
    public Duration getTotalRunningTime(RunningHistory runningHistory) {
        LocalDateTime runningStartedAt = runningHistory.getRunningStartedAt();
        LocalDateTime runningEndAt = runningHistory.getRunningEndAt();

        // 24시간 넘게 달리기를 하는 사람은 없다고 가정하고 Period 클래스를 통한 년/월/일 계산은 하지 않음
        return Duration.between(runningStartedAt, runningEndAt);
    }

    // 달리기 동안의 모든 좌표 값들 구하기
    public List<Coordinate> getAllRunningCoordinates(RunningHistory runningHistory) {
        return runningHistory.getRunningCoordinateList();
    }

    // 1km 단위로 찍힌 좌표 목록 구하기
    public List<Coordinate> getRunningCoordinatesPerKm(RunningHistory runningHistory) {

        List<Coordinate> coordinateList = runningHistory.getRunningCoordinateList();

        // snapshotTime 기준으로 정렬
        coordinateList = coordinateList.stream()
                .sorted(Comparator.comparing(Coordinate::getSnapshotTime))
                .toList();

        Coordinate operandCoordinate = coordinateList.get(0);  // 시작 지점 좌표이니 0km
        double presentDistance = 0;  // 현재까지 달린 거리를 나타낼 변수
        List<Coordinate> runningCoordinatesPerKm = new ArrayList<>();

        for(Coordinate coordinate : coordinateList) {  // 두 지점 사이의 거리를 모두 합하기
            presentDistance += distanceService.getDistanceBetweenPoints(operandCoordinate, coordinate);

            if(presentDistance >= 1) {  // 현재까지의 거리가 1km 이상이 되었다면
                runningCoordinatesPerKm.add(coordinate);
                presentDistance -= 1;  // 아예 0으로 초기화해야 하나? (1.3km에 찍히면 0.3km를 다음 1km 채우는데 포함시켜줘야?
            }

            operandCoordinate = coordinate;
        }

        return runningCoordinatesPerKm;
    }

    // 구간 별 평균 속도 구하기 (이거 구간 당 걸린 시간을 구하라는 건지 아님 구간 당 속력을 구하라는 건지 모르겠음)
    public List<Double> getAvgVelocitiesPerInterval(RunningHistory runningHistory) {
        // getRunningCoordinatesPerKm()로 좌표 가져와서 로직 진행시키지 않은 이유는 애초에 구간 좌표가 정확히 1km가 아니기 떄문에
        // 정확한 거리가 아니여서 (거리 / 시간)으로 정확한 구간 당 속력을 구할 수 없기 때문.

        List<Coordinate> coordinateList = runningHistory.getRunningCoordinateList();

        // snapshotTime 기준으로 정렬
        coordinateList = coordinateList.stream()
                .sorted(Comparator.comparing(Coordinate::getSnapshotTime))
                .toList();

        Coordinate operandCoordinate = coordinateList.get(0);
        double presentDistance = 0;
        LocalDateTime startIntervalPointTime = runningHistory.getRunningStartedAt();
        List<Double> avgVelocitiesPerInterval = new ArrayList<>();

        for(Coordinate coordinate : coordinateList) {  // 두 지점 사이의 거리를 모두 합하기
            presentDistance += distanceService.getDistanceBetweenPoints(operandCoordinate, coordinate);

            if(presentDistance >= 1) {  // 1km 뛴 시점 -> 한 구간
                LocalDateTime endIntervalPointTime = coordinate.getSnapshotTime();

                double runningMin = Duration.between(startIntervalPointTime, endIntervalPointTime).toMinutes();
                double runningSec = Duration.between(startIntervalPointTime, endIntervalPointTime).getSeconds();
                double runningHour = (runningMin/60) + (runningSec/3600);

                double avgVelocity = presentDistance / runningHour;

                avgVelocitiesPerInterval.add(Math.round(avgVelocity*100) / 100.0);

                startIntervalPointTime = endIntervalPointTime;
                presentDistance -= 1;
            }

            operandCoordinate = coordinate;
        }

        return avgVelocitiesPerInterval;
    }

    // km당 평균 속도 구하기
    public Double getAvgVelocityPerKm(RunningHistory runningHistory) {
        double totalRunningDistance = getTotalRunningDistance(runningHistory);
        Duration totalRunningTime = getTotalRunningTime(runningHistory);

        double totalRunningTimeToMin = totalRunningTime.toMinutesPart();
        double totalRunningTimeToSec = totalRunningTime.toSecondsPart();
        double totalRunningTimeToHour = (totalRunningTimeToMin/60) + (totalRunningTimeToSec/3600);

        return Math.round((totalRunningDistance / totalRunningTimeToHour) * 100) / 100.0;
    }

    // 달리기 시작한 시간 구하기
    public LocalDateTime getRunningStartedAt(RunningHistory runningHistory) {
        return runningHistory.getRunningStartedAt();
    }

    // 달리기 종료 시간 구하기
    public LocalDateTime getRunningEndAt(RunningHistory runningHistory) {
        return runningHistory.getRunningEndAt();
    }

}
