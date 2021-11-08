package com.rest.notice.dto;


import com.rest.notice.domain.notice.Notice;
import lombok.Builder;
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
}
