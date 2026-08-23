package com.example.movie.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 影院新增/修改请求
 */
@Data
public class CinemaSaveReq {

    private Long id;
    @NotBlank(message = "影院名称不能为空")
    private String name;
    private String address;
    private String phone;
}
