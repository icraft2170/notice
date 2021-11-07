package com.rest.notice.service.notice;


import com.rest.notice.api.notice.response.NoticeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService{

    public List<NoticeResponse> findAllNotice() {
        return null;
    }
}
