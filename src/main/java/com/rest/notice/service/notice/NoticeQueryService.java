package com.rest.notice.service.notice;

import com.rest.notice.dto.NoticeQueryDto;
import com.rest.notice.dto.NoticesQueryDto;
import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;

public interface NoticeQueryService {
    Page<NoticesQueryDto> findAllNotice(Pageable pageable);
    NoticeQueryDto findOneNotice(Long noticeId);
}
