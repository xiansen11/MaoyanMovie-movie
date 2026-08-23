package com.example.movie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movie.infrastructure.entity.MovieDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 影片 Mapper
 */
@Mapper
public interface MovieMapper extends BaseMapper<MovieDO> {
}
