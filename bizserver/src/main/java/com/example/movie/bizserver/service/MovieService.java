package com.example.movie.bizserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.movie.api.dto.MovieResp;
import com.example.movie.api.dto.MovieSaveReq;
import com.example.movie.api.dto.ScheduleResp;

import java.util.List;

/**
 * 影片服务接口（平台级 CRUD + C 端热映聚合）
 */
public interface MovieService {

    /** 新增影片 */
    Long create(MovieSaveReq req);

    /** 修改影片 */
    void update(MovieSaveReq req);

    /** 删除影片 */
    void delete(Long id);

    /** 分页查询 */
    IPage<MovieResp> page(int pageNum, int pageSize, String title, Integer status);

    /** 详情 */
    MovieResp detail(Long id);

    /** 上下映状态：status 0→1 上映 / 1→2 下映 */
    void changeStatus(Long id, int status);

    /**
     * C 端正在热映：status=1 且当天有有效排片的影片
     */
    List<MovieResp> nowPlaying();

    /**
     * C 端按影片查场次（可按影院筛选）
     */
    List<ScheduleResp> scheduleByMovie(Long movieId, Long cinemaId);

    /**
     * C 端已通过的影院列表
     */
    List<com.example.movie.api.dto.CinemaResp> cinemaList();

    /**
     * Feign：场次详情（无 cinemaId 越权校验，供 order-server 调用）
     */
    ScheduleResp scheduleDetail(Long id);

    /**
     * Feign：释放座位（骨架，实际座位状态在 order 库 t_seat 表，
     * 由 order-server 内部处理；此处保留契约端点）
     */
    void releaseSeats(Long scheduleId, String seatNos);
}
