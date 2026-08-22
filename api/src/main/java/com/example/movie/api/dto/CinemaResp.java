package com.example.movie.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 影院响应
 */
@Data
public class CinemaResp {

    private Long id;
    private String name;
    private String address;
    private String phone;
    /** 0 待审核 / 1 已通过 / 2 已驳回 / 3 已禁用 */
    private Integer status;
    private LocalDateTime createdAt;
}
