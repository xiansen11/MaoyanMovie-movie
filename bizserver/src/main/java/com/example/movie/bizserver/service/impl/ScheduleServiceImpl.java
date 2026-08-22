package com.example.movie.bizserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.exception.BizException;
import com.example.common.response.ResultCode;
import com.example.movie.api.dto.ScheduleResp;
import com.example.movie.api.dto.ScheduleSaveReq;
import com.example.movie.bizserver.service.ScheduleService;
import com.example.movie.infrastructure.entity.CinemaDO;
import com.example.movie.infrastructure.entity.HallDO;
import com.example.movie.infrastructure.entity.ScheduleDO;
import com.example.movie.infrastructure.mapper.CinemaMapper;
import com.example.movie.infrastructure.mapper.HallMapper;
import com.example.movie.infrastructure.mapper.ScheduleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 排片/场次服务实现
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private CinemaMapper cinemaMapper;
    @Autowired
    private HallMapper hallMapper;

    @Override
    public Long create(ScheduleSaveReq req) {
        // 1. 校验影院已审核通过
        CinemaDO cinema = cinemaMapper.selectById(req.getCinemaId());
        if (cinema == null || cinema.getStatus() != 1) {
            throw new BizException(ResultCode.CINEMA_NOT_AUDITED);
        }
        // 2. 校验影厅归属本影院
        HallDO hall = hallMapper.selectById(req.getHallId());
        if (hall == null || !hall.getCinemaId().equals(req.getCinemaId())) {
            throw new BizException(ResultCode.CROSS_CINEMA_ACCESS, "影厅不属于本影院");
        }
        // 3. 校验散场时间晚于放映时间
        if (!req.getEndTime().isAfter(req.getShowTime())) {
            throw new BizException(ResultCode.ORDER_STATUS_ERROR, "散场时间必须晚于放映时间");
        }

        ScheduleDO schedule = new ScheduleDO();
        BeanUtils.copyProperties(req, schedule);
        if (schedule.getStatus() == null) {
            schedule.setStatus(1); // 默认售卖中
        }
        scheduleMapper.insert(schedule);
        log.info("[SCHEDULE-CREATE] 场次新增 id={}, cinemaId={}, movieId={}",
                schedule.getId(), schedule.getCinemaId(), schedule.getMovieId());
        return schedule.getId();
    }

    @Override
    public void update(ScheduleSaveReq req) {
        ScheduleDO exist = scheduleMapper.selectById(req.getId());
        if (exist == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "场次不存在");
        }
        if (!exist.getCinemaId().equals(req.getCinemaId())) {
            throw new BizException(ResultCode.CROSS_CINEMA_ACCESS);
        }
        ScheduleDO schedule = new ScheduleDO();
        BeanUtils.copyProperties(req, schedule);
        scheduleMapper.updateById(schedule);
    }

    @Override
    public void delete(Long id) {
        scheduleMapper.deleteById(id);
    }

    @Override
    public IPage<ScheduleResp> pageByCinema(int pageNum, int pageSize, Long cinemaId, Long movieId) {
        Page<ScheduleDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ScheduleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleDO::getCinemaId, cinemaId);
        if (movieId != null) {
            wrapper.eq(ScheduleDO::getMovieId, movieId);
        }
        wrapper.orderByDesc(ScheduleDO::getShowTime);
        IPage<ScheduleDO> doPage = scheduleMapper.selectPage(page, wrapper);
        Page<ScheduleResp> respPage = new Page<>(pageNum, pageSize, doPage.getTotal());
        respPage.setRecords(doPage.getRecords().stream().map(d -> {
            ScheduleResp r = new ScheduleResp();
            BeanUtils.copyProperties(d, r);
            return r;
        }).toList());
        return respPage;
    }

    @Override
    public ScheduleResp detail(Long id, Long cinemaId) {
        ScheduleDO schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "场次不存在");
        }
        if (!schedule.getCinemaId().equals(cinemaId)) {
            throw new BizException(ResultCode.CROSS_CINEMA_ACCESS);
        }
        ScheduleResp resp = new ScheduleResp();
        BeanUtils.copyProperties(schedule, resp);
        return resp;
    }

    @Override
    public void changeShelf(Long id, int status, Long cinemaId) {
        // status 仅允许 1（上架售卖）/ 4（下架）
        if (status != 1 && status != 4) {
            throw new BizException(ResultCode.ORDER_STATUS_ERROR, "上下架状态非法");
        }
        ScheduleDO exist = scheduleMapper.selectById(id);
        if (exist == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "场次不存在");
        }
        if (!exist.getCinemaId().equals(cinemaId)) {
            throw new BizException(ResultCode.CROSS_CINEMA_ACCESS);
        }
        ScheduleDO update = new ScheduleDO();
        update.setId(id);
        update.setStatus(status);
        scheduleMapper.updateById(update);
        log.info("[SCHEDULE-SHELF] 场次上下架 id={}, status={}", id, status);
    }
}
