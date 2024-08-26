package com.example.running.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter  // Dto를 Json 변환 할 때 필요한 것들 뭐 있는지 (Setter 필요? NoArgs, AllArgs 필요?)
@Builder
public class RunningInfoResponse {
    private LocalDateTime runningStartedAt;
    private LocalDateTime runningEndAt;
    private String totalDistance;
    private String totalTime;
    private String averageVelocityPerKm;
    private List<String> averageVelocitiesPerInterval;
    private List<RunningPoint> coordinateListPerKm;
    private List<RunningPoint> allCoordinateList;

    @Getter
    @Setter
    @Builder
    public static class RunningPoint {
        @Min(value = -90) @Max(value = 90)
        private Double latitude;  // 위도 -> -90 ~ 90

        @Min(value = -180) @Max(value = 180)
        private Double longitude;  // 경도 -> -180 ~ 180
    }
}
