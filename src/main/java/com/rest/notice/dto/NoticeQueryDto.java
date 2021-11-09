package com.rest.notice.dto;


import com.rest.notice.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


//Todo: 생각보다 많은 라이브러리에서 기본생성자를 리플렉션 하는 것으로 보아. 기본생성자를 만들어두는 습관이 필요한듯... [ 블로그 만들고 정리해두기 : JavaType에서 느낀점 - No Constructor Exception]
@Data @NoArgsConstructor @AllArgsConstructor
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
