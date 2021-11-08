package com.rest.notice.repository.notice;

import com.rest.notice.dto.NoticeQueryDto;
import com.rest.notice.domain.notice.Notice;

import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;

public interface NoticeRepository {
    void save(Notice notice);

    public Notice findByNotice(Long noticeId);

    void deleteNotice(Notice notice);

    Page<NoticeQueryDto> findPageNotice(Pageable pageable);
}
