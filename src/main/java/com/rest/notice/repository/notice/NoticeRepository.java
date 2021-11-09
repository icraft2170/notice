package com.rest.notice.repository.notice;

import com.rest.notice.dto.NoticeQueryDto;
import com.rest.notice.dto.NoticesQueryDto;
import com.rest.notice.domain.notice.Notice;

import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;

import java.util.List;

public interface NoticeRepository {
    void save(Notice notice);

    Notice findByNotice(Long noticeId);

    void deleteNotice(Notice notice);

    Page<NoticesQueryDto> findPageNotice(Pageable pageable);

    Notice findOne(Long noticeId);

    List<Notice> findAll();

}
