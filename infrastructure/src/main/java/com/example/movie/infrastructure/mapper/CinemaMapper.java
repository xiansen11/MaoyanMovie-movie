package com.example.movie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movie.infrastructure.entity.CinemaDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 影院 Mapper
 */
@Mapper
public interface CinemaMapper extends BaseMapper<CinemaDO> {
}
