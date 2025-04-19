package org.csu.petstore.service;

import org.csu.petstore.entity.UserLog;

import java.util.List;

public interface UserLogService {
    void recordLog(String username, String behavior);
    List<UserLog> getUserLogs(String username);
}