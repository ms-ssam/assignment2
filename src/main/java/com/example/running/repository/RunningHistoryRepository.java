package com.example.running.repository;

import com.example.running.Entity.RunningHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningHistoryRepository extends JpaRepository<RunningHistory, Long> {
}
