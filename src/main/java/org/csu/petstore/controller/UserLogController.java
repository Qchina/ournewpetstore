package org.csu.petstore.controller;

import jakarta.servlet.http.HttpSession;
import org.csu.petstore.entity.UserLog;
import org.csu.petstore.service.UserLogService;
import org.csu.petstore.vo.AccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class UserLogController {

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private HttpSession session;

    @GetMapping
    public ResponseEntity<List<UserLog>> getUserLogs() {
        AccountVO currentAccount = (AccountVO) session.getAttribute("account");
        if (currentAccount == null) {
            return ResponseEntity.status(401).build();
        }
        List<UserLog> logs = userLogService.getUserLogs(currentAccount.getUsername());
        return ResponseEntity.ok(logs);
    }

    @PostMapping
    public ResponseEntity<Void> recordLog(@RequestParam String behavior) {
        AccountVO currentAccount = (AccountVO) session.getAttribute("account");
        if (currentAccount == null) {
            return ResponseEntity.status(401).build();
        }
        userLogService.recordLog(currentAccount.getUsername(), behavior);
        return ResponseEntity.ok().build();
    }
}