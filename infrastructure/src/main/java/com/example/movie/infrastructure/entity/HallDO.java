package com.example.movie.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 影厅表（归属影院）
 */
@Data
@TableName("t_hall")
public class HallDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long cinemaId;
    private String name;
    private Integer rows;
    private Integer cols;
    private String rowAlias;
}
