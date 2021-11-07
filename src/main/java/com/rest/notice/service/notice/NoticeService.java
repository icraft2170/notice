package com.rest.notice.service.notice;

import com.rest.notice.api.notice.request.NoticeRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoticeService {
    String saveNotice(NoticeRequest request, List<MultipartFile> files);

    String modifyNotice(Long noticeId, NoticeRequest request, List<MultipartFile> files);

    String deleteNotice(Long noticeId);
}
