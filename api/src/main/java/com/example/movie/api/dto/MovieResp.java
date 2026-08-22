package com.example.movie.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 影片响应
 */
@Data
public class MovieResp {

    private Long id;
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
    private Integer status;
    private LocalDateTime createdAt;
}
