package com.example.movie.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 影片基础数据表（平台级，所有影院共用）
 */
@Data
@TableName("t_movie")
public class MovieDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String poster;
    private String genre;
    private Integer duration;
    private LocalDate releaseDate;
    private LocalDate offDate;
    private String director;
    private String actors;
    private String synopsis;
    private Double rating;

    /** 0 即将上映 / 1 正在热映 / 2 已下映 */
    private Integer status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
