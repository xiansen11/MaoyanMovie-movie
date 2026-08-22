package com.example.movie.apiserver.controller.admin.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.response.Result;
import com.example.movie.api.dto.MovieResp;
import com.example.movie.api.dto.MovieSaveReq;
import com.example.movie.bizserver.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 平台后台影片接口 /admin/platform/movie
 */
@RestController
@RequestMapping("/admin/platform/movie")
public class MoviePlatformAdminController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public Result<Long> create(@Valid @RequestBody MovieSaveReq req) {
        return Result.success(movieService.create(req));
    }

    @PutMapping
    public Result<Void> update(@Valid @RequestBody MovieSaveReq req) {
        movieService.update(req);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<IPage<MovieResp>> page(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String title,
                                          @RequestParam(required = false) Integer status) {
        return Result.success(movieService.page(pageNum, pageSize, title, status));
    }

    @GetMapping("/{id}")
    public Result<MovieResp> detail(@PathVariable Long id) {
        return Result.success(movieService.detail(id));
    }

    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam int status) {
        movieService.changeStatus(id, status);
        return Result.success();
    }
}
