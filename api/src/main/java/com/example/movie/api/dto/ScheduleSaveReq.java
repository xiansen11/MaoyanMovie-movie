package com.example.movie.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 排片新增/修改请求
 */
@Data
public class ScheduleSaveReq {

    private Long id;
    @NotNull(message = "影片 ID 不能为空")
    private Long movieId;
    @NotNull(message = "影院 ID 不能为空")
    private Long cinemaId;
    @NotNull(message = "影厅 ID 不能为空")
    private Long hallId;
    @NotNull(message = "放映时间不能为空")
    private LocalDateTime showTime;
    @NotNull(message = "散场时间不能为空")
    private LocalDateTime endTime;
    @NotNull(message = "票价不能为空")
    private BigDecimal price;
    /** 0 未开始 / 1 售卖中 / 4 已下架（新建默认 1） */
    private Integer status;
}
