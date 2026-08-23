package com.example.movie.bizserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.movie.api.dto.CinemaResp;
import com.example.movie.api.dto.CinemaSaveReq;

/**
 * 影院服务接口
 */
public interface CinemaService {

    /** 影院申请入驻（status=0 待审核） */
    Long apply(CinemaSaveReq req);

    /** 修改影院基础信息 */
    void update(CinemaSaveReq req);

    /** 影院列表（按状态筛选） */
    IPage<CinemaResp> page(int pageNum, int pageSize, Integer status);

    /** 影院详情 */
    CinemaResp detail(Long id);

    /**
     * 影院入驻审核
     * @param cinemaId 影院 ID
     * @param status   1 已通过 / 2 已驳回 / 3 已禁用
     * @param adminUsername 审核通过时为影院创建的账号名（status=1 时必填）
     * @param adminPassword  对应密码
     */
    void audit(Long cinemaId, int status, String adminUsername, String adminPassword);
}
