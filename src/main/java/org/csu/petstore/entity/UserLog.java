package org.csu.petstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mylog")
public class UserLog {
    private Long id;
    private String username;
    private String behavior;
    private LocalDateTime time;
}