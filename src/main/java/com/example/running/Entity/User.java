package com.example.running.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User : RunningHistory -> 1:N (사용자 : 달리기 기록은 일대다 관계)
 * RunningHistory : Coordinate -> 1:N (달리기 기록 : 러닝 좌표는 일대다 관계)
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<RunningHistory> runningHistories = new ArrayList<>();

    public void addRunningHistory(RunningHistory runningHistory) {
        runningHistories.add(runningHistory);
    }
}
