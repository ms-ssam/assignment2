package com.example.running.controller;

import com.example.running.Service.RequestProcessService;
import com.example.running.dto.RunningInfoRequest;
import com.example.running.dto.RunningInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class RunningController {

    private final RequestProcessService requestProcessService;

    // 해당 사용자의 달리기 내역 저장
    @PostMapping("/{userId}")
    public void createRunningHistory(@PathVariable Long userId,
                                     @RequestBody RunningInfoRequest request) {

        requestProcessService.updateUserRunningHistory(userId, request);
        // 제대로 실행됐는지 확인시켜주기 위해 뭐 return 해주거나 해야하나?
    }

    // 해당 사용자의 특정 달리기 기록 가져오기
    @GetMapping("/{userId}/runnings")
    public RunningInfoResponse getRunningHistory(@PathVariable Long userId,
                                                 @RequestParam("running-id") Long runningId) {

        return requestProcessService.selectUserRunningHistory(userId, runningId);
    }
}
