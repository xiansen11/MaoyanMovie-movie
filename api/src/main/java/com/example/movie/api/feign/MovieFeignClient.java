package com.example.movie.api.feign;

import com.example.common.response.Result;
import com.example.movie.api.dto.ScheduleResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 影片服务 Feign 客户端（供 order-server 调用）
 * 路径前缀 /feign/movie，网关不暴露
 */
@FeignClient(name = "movie-server", contextId = "movieFeignClient", path = "/feign/movie")
public interface MovieFeignClient {

    /**
     * 场次详情（含影院/影厅校验）
     */
    @GetMapping("/schedule/{id}")
    Result<ScheduleResp> scheduleDetail(@PathVariable("id") Long id);

    /**
     * 释放座位（取消/退票时由 order-server 调用）
     * 此接口在 movie-server 端将对应场次座位状态重置
     * @param scheduleId 场次 ID
     * @param seatNos    座位号列表
     */
    @PutMapping("/seat/release")
    Result<Void> releaseSeats(@RequestParam("scheduleId") Long scheduleId,
                              @RequestParam("seatNos") String seatNos);
}
