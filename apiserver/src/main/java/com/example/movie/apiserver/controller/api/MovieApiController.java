package com.example.movie.apiserver.controller.api;

import com.example.common.response.Result;
import com.example.movie.api.dto.CinemaResp;
import com.example.movie.api.dto.MovieResp;
import com.example.movie.api.dto.ScheduleResp;
import com.example.movie.bizserver.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C 端影片接口 /api/movie（公开，无需鉴权）
 */
@RestController
@RequestMapping("/api/movie")
public class MovieApiController {

    @Autowired
    private MovieService movieService;

    /** 正在热映列表（status=1 且当天有有效排片） */
    @GetMapping("/now-playing")
    public Result<List<MovieResp>> nowPlaying() {
        return Result.success(movieService.nowPlaying());
    }

    /** 影片详情 */
    @GetMapping("/detail/{id}")
    public Result<MovieResp> detail(@PathVariable Long id) {
        return Result.success(movieService.detail(id));
    }

    /** 场次列表（可按影院筛选） */
    @GetMapping("/schedule/{movieId}")
    public Result<List<ScheduleResp>> schedule(@PathVariable Long movieId,
                                                  @RequestParam(required = false) Long cinemaId) {
        return Result.success(movieService.scheduleByMovie(movieId, cinemaId));
    }

    /** 已通过的影院列表 */
    @GetMapping("/cinema/list")
    public Result<List<CinemaResp>> cinemaList() {
        return Result.success(movieService.cinemaList());
    }
}
