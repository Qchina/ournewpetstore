package org.csu.petstore.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.csu.petstore.entity.UserLog;

@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {
}