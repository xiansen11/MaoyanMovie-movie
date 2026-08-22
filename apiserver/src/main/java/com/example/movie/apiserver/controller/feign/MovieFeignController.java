package com.example.movie.apiserver.controller.feign;

import com.example.common.response.Result;
import com.example.movie.api.dto.ScheduleResp;
import com.example.movie.api.feign.MovieFeignClient;
import com.example.movie.bizserver.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 影片服务 Feign 接口实现 /feign/movie（不经网关，服务间调用）
 */
@RestController
@RequestMapping("/feign/movie")
public class MovieFeignController implements MovieFeignClient {

    @Autowired
    private MovieService movieService;

    @Override
    @GetMapping("/schedule/{id}")
    public Result<ScheduleResp> scheduleDetail(@PathVariable("id") Long id) {
        return Result.success(movieService.scheduleDetail(id));
    }

    @Override
    @PutMapping("/seat/release")
    public Result<Void> releaseSeats(@RequestParam("scheduleId") Long scheduleId,
                                     @RequestParam("seatNos") String seatNos) {
        movieService.releaseSeats(scheduleId, seatNos);
        return Result.success();
    }
}
