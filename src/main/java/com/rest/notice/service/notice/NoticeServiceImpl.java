package com.rest.notice.service.notice;

import com.rest.notice.service.file.FileNotice;
import com.rest.notice.api.notice.request.NoticeRequest;
import com.rest.notice.domain.notice.Notice;
import com.rest.notice.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service @Transactional
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final FileNotice fileNotice;

    @Override
    public void saveNotice(NoticeRequest request,List<MultipartFile> files) {
        noticeRepository.save(Notice.createNotice(request.getTitle(), request.getContent(),
                request.getWriter(), request.getEndDate(), fileNotice.repositoryFile(files)));
    }

    @Override
    public void modifyNotice(Long noticeId, NoticeRequest request, List<MultipartFile> files) {
        Notice notice = noticeRepository.findByNotice(noticeId);

        // 현재 저장 파일 전체삭제
        notice.getUploadFiles().forEach(uploadFile -> fileNotice.deleteFile(uploadFile.getRepositoryFileName()));
        notice.setUpdate(request, fileNotice.repositoryFile(files));
    }

    @Override
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteNotice(noticeRepository.findByNotice(noticeId));
    }
}
