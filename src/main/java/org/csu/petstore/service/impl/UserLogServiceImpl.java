package org.csu.petstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.petstore.entity.UserLog;
import org.csu.petstore.persistence.UserLogMapper;
import org.csu.petstore.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public void recordLog(String username, String behavior) {
        UserLog log = new UserLog();
        log.setUsername(username);
        log.setBehavior(behavior);
        log.setTime(LocalDateTime.now());
        userLogMapper.insert(log);
    }

    @Override
    public List<UserLog> getUserLogs(String username) {
        QueryWrapper<UserLog> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
               .orderByDesc("time");
        return userLogMapper.selectList(wrapper);
    }
}