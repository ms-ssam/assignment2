package com.example.running.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coordinate {

    @Id @GeneratedValue
    private Long id;

    private Double latitude;

    private Double longitude;

    private Double height;

    private LocalDateTime snapshotTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private RunningHistory runningHistory;
}
