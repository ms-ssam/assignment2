package com.example.running.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RunningHistory {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime runningStartedAt;

    private LocalDateTime runningEndAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "runningHistory")
    private List<Coordinate> runningCoordinateList = new ArrayList<>();

    public void addCoordinate(Coordinate coordinate) {
        runningCoordinateList.add(coordinate);
    }
}
