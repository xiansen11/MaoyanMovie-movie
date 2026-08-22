package com.example.movie.apiserver.controller.admin.cinema;

import com.example.common.response.Result;
import com.example.movie.api.dto.HallSaveReq;
import com.example.movie.api.dto.ScheduleResp;
import com.example.movie.api.dto.ScheduleSaveReq;
import com.example.movie.bizserver.service.HallService;
import com.example.movie.bizserver.service.ScheduleService;
import com.example.movie.infrastructure.entity.HallDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 影院后台排片/影厅接口 /admin/cinema
 */
@RestController
@RequestMapping("/admin/cinema")
public class MovieCinemaAdminController {

    @Autowired
    private HallService hallService;
    @Autowired
    private ScheduleService scheduleService;

    // ===== 影厅管理 =====

    @PostMapping("/hall")
    public Result<Long> createHall(@Valid @RequestBody HallSaveReq req) {
        return Result.success(hallService.create(req));
    }

    @PutMapping("/hall")
    public Result<Void> updateHall(@Valid @RequestBody HallSaveReq req) {
        hallService.update(req);
        return Result.success();
    }

    @DeleteMapping("/hall/{id}")
    public Result<Void> deleteHall(@PathVariable Long id) {
        hallService.delete(id);
        return Result.success();
    }

    @GetMapping("/hall/list")
    public Result<List<HallDO>> listHall(@RequestParam Long cinemaId) {
        return Result.success(hallService.listByCinema(cinemaId));
    }

    @GetMapping("/hall/{id}")
    public Result<HallDO> detailHall(@PathVariable Long id, @RequestParam Long cinemaId) {
        return Result.success(hallService.detail(id, cinemaId));
    }

    // ===== 排片管理 =====

    @PostMapping("/schedule")
    public Result<Long> createSchedule(@Valid @RequestBody ScheduleSaveReq req) {
        return Result.success(scheduleService.create(req));
    }

    @PutMapping("/schedule")
    public Result<Void> updateSchedule(@Valid @RequestBody ScheduleSaveReq req) {
        scheduleService.update(req);
        return Result.success();
    }

    @DeleteMapping("/schedule/{id}")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return Result.success();
    }

    @GetMapping("/schedule/list")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<ScheduleResp>> pageSchedule(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Long cinemaId,
            @RequestParam(required = false) Long movieId) {
        return Result.success(scheduleService.pageByCinema(pageNum, pageSize, cinemaId, movieId));
    }

    @GetMapping("/schedule/{id}")
    public Result<ScheduleResp> detailSchedule(@PathVariable Long id, @RequestParam Long cinemaId) {
        return Result.success(scheduleService.detail(id, cinemaId));
    }

    /** 场次上下架 status: 1 上架售卖 / 4 下架 */
    @PutMapping("/schedule/{id}/shelf")
    public Result<Void> changeShelf(@PathVariable Long id, @RequestParam int status, @RequestParam Long cinemaId) {
        scheduleService.changeShelf(id, status, cinemaId);
        return Result.success();
    }
}
