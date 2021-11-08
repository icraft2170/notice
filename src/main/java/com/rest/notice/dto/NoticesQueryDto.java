package com.rest.notice.dto;


import com.rest.notice.domain.notice.Notice;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticesQueryDto {
    private String title;
    private LocalDateTime registrationDate;
    private Integer hit;
    private String writer;

    public NoticesQueryDto(Notice notice) {
        this.title = notice.getTitle();
        this.registrationDate = notice.getRegistrationDate();
        this.hit = notice.getHit();
        this.writer = notice.getWriter();
    }
}
