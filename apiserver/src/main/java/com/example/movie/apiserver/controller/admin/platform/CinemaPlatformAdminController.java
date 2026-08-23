package com.example.movie.apiserver.controller.admin.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.response.Result;
import com.example.movie.api.dto.CinemaResp;
import com.example.movie.api.dto.CinemaSaveReq;
import com.example.movie.bizserver.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 平台后台影院入驻审核接口 /admin/platform/cinema
 */
@RestController
@RequestMapping("/admin/platform/cinema")
public class CinemaPlatformAdminController {

    @Autowired
    private CinemaService cinemaService;

    /** 影院列表（按状态筛选） */
    @GetMapping("/list")
    public Result<IPage<CinemaResp>> page(@RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(required = false) Integer status) {
        return Result.success(cinemaService.page(pageNum, pageSize, status));
    }

    @GetMapping("/{id}")
    public Result<CinemaResp> detail(@PathVariable Long id) {
        return Result.success(cinemaService.detail(id));
    }

    /**
     * 影院入驻审核
     * @param id     影院 ID
     * @param status 1 已通过 / 2 已驳回 / 3 已禁用
     * @param adminUsername 审核通过时创建的影院账号名（status=1 必填）
     * @param adminPassword  对应密码（status=1 必填）
     */
    @PostMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id,
                              @RequestParam int status,
                              @RequestParam(required = false) String adminUsername,
                              @RequestParam(required = false) String adminPassword) {
        cinemaService.audit(id, status, adminUsername, adminPassword);
        return Result.success();
    }

    /** 影院入驻申请（也可由平台直接录入） */
    @PostMapping
    public Result<Long> apply(@Valid @RequestBody CinemaSaveReq req) {
        return Result.success(cinemaService.apply(req));
    }

    /** 修改影院信息 */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CinemaSaveReq req) {
        cinemaService.update(req);
        return Result.success();
    }
}
