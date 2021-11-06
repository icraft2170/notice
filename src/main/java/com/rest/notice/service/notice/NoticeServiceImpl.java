package com.rest.notice.service.notice;

import com.rest.notice.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;


}
