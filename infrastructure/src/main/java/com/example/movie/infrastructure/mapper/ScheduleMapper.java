package com.example.movie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movie.infrastructure.entity.ScheduleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 排片/场次 Mapper
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<ScheduleDO> {
}
