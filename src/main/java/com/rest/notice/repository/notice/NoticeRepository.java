package com.rest.notice.repository.notice;

import com.rest.notice.domain.notice.Notice;

public interface NoticeRepository {
    void save(Notice notice);

    public Notice findByNotice(Long noticeId);
}
