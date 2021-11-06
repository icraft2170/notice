package com.rest.notice.api.notice;

import com.rest.notice.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NoticeApiController {

    private final NoticeService noticeService;
    



}
