package com.example.movie.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 影片新增/修改请求
 */
@Data
public class MovieSaveReq {

    private Long id;
    @NotBlank(message = "片名不能为空")
    private String title;
    private String poster;
    private String genre;
    private Integer duration;
    private LocalDate releaseDate;
    private LocalDate offDate;
    private String director;
    private String actors;
    private String synopsis;
    private Double rating;

    /** 0 即将上映 / 1 正在热映 / 2 已下映 */
    private Integer status;
}
