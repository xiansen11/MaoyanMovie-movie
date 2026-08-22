package com.example.movie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movie.infrastructure.entity.HallDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 影厅 Mapper
 */
@Mapper
public interface HallMapper extends BaseMapper<HallDO> {
}
