package com.rest.notice.service.notice;

import com.rest.notice.api.notice.response.NoticeResponse;

import java.util.List;

public interface NoticeQueryService {
    List<NoticeResponse> findAllNotice();
}
