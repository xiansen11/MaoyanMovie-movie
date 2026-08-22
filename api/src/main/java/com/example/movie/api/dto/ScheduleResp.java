package com.example.movie.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 场次响应
 */
@Data
public class ScheduleResp {

    private Long id;
    private Long movieId;
    private Long cinemaId;
    private Long hallId;
    private LocalDateTime showTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private Integer status;
    private String movieTitle;  // 冗余便于展示
    private String cinemaName;
    private String hallName;
}
