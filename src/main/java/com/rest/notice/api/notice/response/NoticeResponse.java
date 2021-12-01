package com.rest.notice.api.notice.response;

import lombok.*;

@NoArgsConstructor
@Getter @Setter
public class NoticeResponse {
    String message;

    @Builder
    public NoticeResponse(String message) {
        this.message = message;
    }
}
