package com.example.movie.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 影院表（影院入驻与基础信息）
 */
@Data
@TableName("t_cinema")
public class CinemaDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String address;
    private String phone;

    /** 0 待审核 / 1 已通过 / 2 已驳回 / 3 已禁用 */
    private Integer status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
