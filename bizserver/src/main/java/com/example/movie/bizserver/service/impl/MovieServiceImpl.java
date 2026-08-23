package com.example.movie.bizserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.exception.BizException;
import com.example.common.response.ResultCode;
import com.example.movie.api.dto.CinemaResp;
import com.example.movie.api.dto.MovieResp;
import com.example.movie.api.dto.MovieSaveReq;
import com.example.movie.api.dto.ScheduleResp;
import com.example.movie.bizserver.service.MovieService;
import com.example.movie.infrastructure.entity.CinemaDO;
import com.example.movie.infrastructure.entity.MovieDO;
import com.example.movie.infrastructure.entity.ScheduleDO;
import com.example.movie.infrastructure.mapper.CinemaMapper;
import com.example.movie.infrastructure.mapper.MovieMapper;
import com.example.movie.infrastructure.mapper.ScheduleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 影片服务实现
 */
@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private CinemaMapper cinemaMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public Long create(MovieSaveReq req) {
        MovieDO movie = new MovieDO();
        BeanUtils.copyProperties(req, movie);
        if (movie.getStatus() == null) {
            movie.setStatus(0); // 默认即将上映
        }
        movieMapper.insert(movie);
        log.info("[MOVIE-CREATE] 影片新增 id={}, title={}", movie.getId(), movie.getTitle());
        return movie.getId();
    }

    @Override
    public void update(MovieSaveReq req) {
        MovieDO exist = movieMapper.selectById(req.getId());
        if (exist == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影片不存在");
        }
        MovieDO movie = new MovieDO();
        BeanUtils.copyProperties(req, movie);
        movieMapper.updateById(movie);
        log.info("[MOVIE-UPDATE] 影片修改 id={}", req.getId());
    }

    @Override
    public void delete(Long id) {
        movieMapper.deleteById(id);
        log.info("[MOVIE-DELETE] 影片删除 id={}", id);
    }

    @Override
    public IPage<MovieResp> page(int pageNum, int pageSize, String title, Integer status) {
        Page<MovieDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MovieDO> wrapper = new LambdaQueryWrapper<>();
        if (title != null && !title.isBlank()) {
            wrapper.like(MovieDO::getTitle, title);
        }
        if (status != null) {
            wrapper.eq(MovieDO::getStatus, status);
        }
        wrapper.orderByDesc(MovieDO::getCreatedAt);
        IPage<MovieDO> doPage = movieMapper.selectPage(page, wrapper);
        // 转换 DO → Resp
        Page<MovieResp> respPage = new Page<>(pageNum, pageSize, doPage.getTotal());
        respPage.setRecords(doPage.getRecords().stream().map(d -> {
            MovieResp r = new MovieResp();
            BeanUtils.copyProperties(d, r);
            return r;
        }).toList());
        return respPage;
    }

    @Override
    public MovieResp detail(Long id) {
        MovieDO movie = movieMapper.selectById(id);
        if (movie == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影片不存在");
        }
        MovieResp resp = new MovieResp();
        BeanUtils.copyProperties(movie, resp);
        return resp;
    }

    @Override
    public void changeStatus(Long id, int status) {
        MovieDO movie = movieMapper.selectById(id);
        if (movie == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影片不存在");
        }
        MovieDO update = new MovieDO();
        update.setId(id);
        update.setStatus(status);
        movieMapper.updateById(update);
        log.info("[MOVIE-STATUS] 影片状态变更 id={}, status={}", id, status);
    }

    @Override
    public List<MovieResp> nowPlaying() {
        // status=1 正在热映
        List<MovieDO> movies = movieMapper.selectList(
                new LambdaQueryWrapper<MovieDO>()
                        .eq(MovieDO::getStatus, 1)
                        .orderByDesc(MovieDO::getRating));
        // 仅保留当天有有效排片（show_time 当天且 status=1 售卖中）的影片
        LocalDate today = LocalDate.now();
        return movies.stream().filter(m -> {
            Long count = scheduleMapper.selectCount(
                    new LambdaQueryWrapper<ScheduleDO>()
                            .eq(ScheduleDO::getMovieId, m.getId())
                            .eq(ScheduleDO::getStatus, 1)
                            .between(ScheduleDO::getShowTime,
                                    today.atStartOfDay(),
                                    today.plusDays(1).atStartOfDay()));
            return count != null && count > 0;
        }).map(d -> {
            MovieResp r = new MovieResp();
            BeanUtils.copyProperties(d, r);
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResp> scheduleByMovie(Long movieId, Long cinemaId) {
        LambdaQueryWrapper<ScheduleDO> wrapper = new LambdaQueryWrapper<ScheduleDO>()
                .eq(ScheduleDO::getMovieId, movieId)
                .eq(ScheduleDO::getStatus, 1) // 售卖中
                .gt(ScheduleDO::getShowTime, LocalDateTime.now())
                .orderByAsc(ScheduleDO::getShowTime);
        if (cinemaId != null) {
            wrapper.eq(ScheduleDO::getCinemaId, cinemaId);
        }
        List<ScheduleDO> schedules = scheduleMapper.selectList(wrapper);
        return schedules.stream().map(d -> {
            ScheduleResp r = new ScheduleResp();
            BeanUtils.copyProperties(d, r);
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CinemaResp> cinemaList() {
        List<CinemaDO> cinemas = cinemaMapper.selectList(
                new LambdaQueryWrapper<CinemaDO>()
                        .eq(CinemaDO::getStatus, 1) // 已通过
                        .orderByDesc(CinemaDO::getCreatedAt));
        return cinemas.stream().map(d -> {
            CinemaResp r = new CinemaResp();
            BeanUtils.copyProperties(d, r);
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public ScheduleResp scheduleDetail(Long id) {
        ScheduleDO schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "场次不存在");
        }
        ScheduleResp resp = new ScheduleResp();
        BeanUtils.copyProperties(schedule, resp);
        return resp;
    }

    @Override
    public void releaseSeats(Long scheduleId, String seatNos) {
        // 座位表 t_seat 在 db_order 库，实际由 order-server 处理
        // 此端点保留作为服务间契约，未来若 movie 维护座位再实现
        log.info("[MOVIE-SEAT-RELEASE] 占位调用 scheduleId={}, seatNos={}", scheduleId, seatNos);
    }
}
