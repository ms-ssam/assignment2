package com.example.running.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RunningInfoRequest {
    private LocalDateTime runningStartedAt;
    private LocalDateTime runningEndAt;
    private List<RunningPointInfo> runningPointList;

    @Getter
    @Setter
    @Builder
    public static class RunningPointInfo {
        @Min(value = -90) @Max(value = 90)
        private Double latitude;  // 위도 -> -90 ~ 90

        @Min(value = -180) @Max(value = 180)
        private Double longitude;  // 경도 -> -180 ~ 180

        private Double height;

        private LocalDateTime snapshotTime;
    }
}
