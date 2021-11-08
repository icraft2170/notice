package com.rest.notice.service.notice;


import com.rest.notice.dto.NoticeQueryDto;
import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;
import com.rest.notice.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service @Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService{

    private final NoticeRepository noticeRepository;

    @Override
    public Page<NoticeQueryDto> findAllNotice(Pageable pageable) {
        return noticeRepository.findPageNotice(pageable);
    }
}
