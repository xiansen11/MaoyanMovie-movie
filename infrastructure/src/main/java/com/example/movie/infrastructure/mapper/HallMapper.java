package com.example.movie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movie.infrastructure.entity.HallDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 影厅 Mapper
 */
@Mapper
public interface HallMapper extends BaseMapper<HallDO> {

    @Select("SELECT id, cinema_id, name, `rows`, `cols`, row_alias FROM t_hall WHERE cinema_id = #{cinemaId}")
    List<HallDO> listByCinema(@Param("cinemaId") Long cinemaId);
}
