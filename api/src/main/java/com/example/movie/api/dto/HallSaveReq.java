package com.example.movie.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 影厅新增/修改请求
 */
@Data
public class HallSaveReq {

    private Long id;
    @NotNull(message = "影院 ID 不能为空")
    private Long cinemaId;
    @NotBlank(message = "影厅名不能为空")
    private String name;
    @NotNull(message = "行数不能为空")
    private Integer rows;
    @NotNull(message = "列数不能为空")
    private Integer cols;
    private String rowAlias;
}
