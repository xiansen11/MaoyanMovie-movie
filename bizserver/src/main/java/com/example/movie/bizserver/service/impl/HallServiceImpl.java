package com.example.movie.bizserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.exception.BizException;
import com.example.common.response.ResultCode;
import com.example.movie.api.dto.HallSaveReq;
import com.example.movie.bizserver.service.HallService;
import com.example.movie.infrastructure.entity.HallDO;
import com.example.movie.infrastructure.mapper.HallMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 影厅服务实现
 */
@Slf4j
@Service
public class HallServiceImpl implements HallService {

    @Autowired
    private HallMapper hallMapper;

    @Override
    public Long create(HallSaveReq req) {
        HallDO hall = new HallDO();
        BeanUtils.copyProperties(req, hall);
        hallMapper.insert(hall);
        log.info("[HALL-CREATE] 影厅新增 id={}, cinemaId={}", hall.getId(), hall.getCinemaId());
        return hall.getId();
    }

    @Override
    public void update(HallSaveReq req) {
        HallDO exist = hallMapper.selectById(req.getId());
        if (exist == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影厅不存在");
        }
        // 越权校验：cinemaId 必须匹配
        if (!exist.getCinemaId().equals(req.getCinemaId())) {
            throw new BizException(ResultCode.CROSS_CINEMA_ACCESS);
        }
        HallDO hall = new HallDO();
        BeanUtils.copyProperties(req, hall);
        hallMapper.updateById(hall);
    }

    @Override
    public void delete(Long id) {
        hallMapper.deleteById(id);
    }

    @Override
    public List<HallDO> listByCinema(Long cinemaId) {
        return hallMapper.selectList(
                new LambdaQueryWrapper<HallDO>().eq(HallDO::getCinemaId, cinemaId));
    }

    @Override
    public HallDO detail(Long id, Long cinemaId) {
        HallDO hall = hallMapper.selectById(id);
        if (hall == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影厅不存在");
        }
        // 越权校验
        if (!hall.getCinemaId().equals(cinemaId)) {
            throw new BizException(ResultCode.CROSS_CINEMA_ACCESS);
        }
        return hall;
    }
}
