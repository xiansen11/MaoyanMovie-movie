package com.example.movie.bizserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.movie.api.dto.ScheduleResp;
import com.example.movie.api.dto.ScheduleSaveReq;

/**
 * 排片/场次服务接口
 */
public interface ScheduleService {

    /** 创建场次（校验影院已审核 + 影厅归属本影院） */
    Long create(ScheduleSaveReq req);

    /** 修改场次 */
    void update(ScheduleSaveReq req);

    /** 删除场次 */
    void delete(Long id);

    /** 按影院分页查询场次（影院后台） */
    IPage<ScheduleResp> pageByCinema(int pageNum, int pageSize, Long cinemaId, Long movieId);

    /** 场次详情（按影院校验归属） */
    ScheduleResp detail(Long id, Long cinemaId);

    /** 场次上下架：status 1↔4 */
    void changeShelf(Long id, int status, Long cinemaId);
}
