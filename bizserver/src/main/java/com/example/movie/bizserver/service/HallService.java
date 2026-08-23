package com.example.movie.bizserver.service;

import com.example.movie.api.dto.HallSaveReq;
import com.example.movie.infrastructure.entity.HallDO;

import java.util.List;

/**
 * 影厅服务接口
 */
public interface HallService {

    /** 新增影厅 */
    Long create(HallSaveReq req);

    /** 修改影厅 */
    void update(HallSaveReq req);

    /** 删除影厅 */
    void delete(Long id);

    /** 查询某影院的影厅列表 */
    List<HallDO> listByCinema(Long cinemaId);

    /** 查询影厅详情（含归属校验：cinemaId 必须匹配） */
    HallDO detail(Long id, Long cinemaId);
}
