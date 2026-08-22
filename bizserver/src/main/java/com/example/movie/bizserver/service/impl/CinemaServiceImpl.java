package com.example.movie.bizserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.exception.BizException;
import com.example.common.response.ResultCode;
import com.example.movie.api.dto.CinemaResp;
import com.example.movie.api.dto.CinemaSaveReq;
import com.example.movie.bizserver.service.CinemaService;
import com.example.movie.infrastructure.entity.CinemaDO;
import com.example.movie.infrastructure.mapper.CinemaMapper;
import com.example.user.api.dto.CinemaAccountReq;
import com.example.user.api.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 影院服务实现
 */
@Slf4j
@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public Long apply(CinemaSaveReq req) {
        CinemaDO cinema = new CinemaDO();
        BeanUtils.copyProperties(req, cinema);
        cinema.setStatus(0); // 待审核
        cinemaMapper.insert(cinema);
        log.info("[CINEMA-APPLY] 影院入驻申请 id={}, name={}", cinema.getId(), cinema.getName());
        return cinema.getId();
    }

    @Override
    public void update(CinemaSaveReq req) {
        CinemaDO exist = cinemaMapper.selectById(req.getId());
        if (exist == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影院不存在");
        }
        CinemaDO cinema = new CinemaDO();
        BeanUtils.copyProperties(req, cinema);
        cinemaMapper.updateById(cinema);
    }

    @Override
    public IPage<CinemaResp> page(int pageNum, int pageSize, Integer status) {
        Page<CinemaDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CinemaDO> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(CinemaDO::getStatus, status);
        }
        wrapper.orderByDesc(CinemaDO::getCreatedAt);
        IPage<CinemaDO> doPage = cinemaMapper.selectPage(page, wrapper);
        Page<CinemaResp> respPage = new Page<>(pageNum, pageSize, doPage.getTotal());
        respPage.setRecords(doPage.getRecords().stream().map(d -> {
            CinemaResp r = new CinemaResp();
            BeanUtils.copyProperties(d, r);
            return r;
        }).toList());
        return respPage;
    }

    @Override
    public CinemaResp detail(Long id) {
        CinemaDO cinema = cinemaMapper.selectById(id);
        if (cinema == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影院不存在");
        }
        CinemaResp resp = new CinemaResp();
        BeanUtils.copyProperties(cinema, resp);
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(Long cinemaId, int status, String adminUsername, String adminPassword) {
        CinemaDO cinema = cinemaMapper.selectById(cinemaId);
        if (cinema == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "影院不存在");
        }
        // 仅待审核或已禁用影院可审核
        if (cinema.getStatus() != 0 && cinema.getStatus() != 3) {
            throw new BizException(ResultCode.ORDER_STATUS_ERROR, "影院当前状态不允许审核");
        }

        CinemaDO update = new CinemaDO();
        update.setId(cinemaId);
        update.setStatus(status);
        cinemaMapper.updateById(update);

        // 审核通过：经 Feign 调 user-server 创建影院工作人员账号（role=2）
        if (status == 1) {
            CinemaAccountReq req = new CinemaAccountReq();
            req.setUsername(adminUsername);
            req.setPassword(adminPassword);
            req.setCinemaId(cinemaId);
            req.setNickname(cinema.getName() + "-工作人员");
            userFeignClient.createCinemaAccount(req);
            log.info("[CINEMA-AUDIT] 影院审核通过 id={}, 创建影院账号 username={}", cinemaId, adminUsername);
        } else {
            log.info("[CINEMA-AUDIT] 影院审核 id={}, status={}", cinemaId, status);
        }
    }
}
