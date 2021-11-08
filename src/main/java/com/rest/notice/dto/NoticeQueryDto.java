package com.rest.notice.dto;


import com.rest.notice.domain.notice.Notice;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeQueryDto {
    private String title;
    private String content;
    private LocalDateTime registrationDate;
    private Integer hit;
    private String writer;

    public NoticeQueryDto(Notice notice) {
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.registrationDate = notice.getRegistrationDate();
        this.hit = notice.getHit();
        this.writer = notice.getWriter();
    }
    public NoticeQueryDto(String title, String content, LocalDateTime registrationDate,Integer hit, String writer) {
        this.title = title;
        this.content = content;
        this.registrationDate = registrationDate;
        this.hit = hit;
        this.writer = writer;
    }
}
