package com.example.movie.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 排片/场次表（归属影院 + 影片）
 */
@Data
@TableName("t_schedule")
public class ScheduleDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long movieId;
    private Long cinemaId;
    private Long hallId;
    private LocalDateTime showTime;
    private LocalDateTime endTime;
    private BigDecimal price;

    /** 0 未开始 / 1 售卖中 / 2 已售罄 / 3 已结束 / 4 已下架 */
    private Integer status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
