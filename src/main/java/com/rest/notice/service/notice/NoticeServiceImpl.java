package com.rest.notice.service.notice;

import com.rest.notice.api.notice.file.FileNotice;
import com.rest.notice.api.notice.request.NoticeRequest;
import com.rest.notice.domain.notice.Notice;
import com.rest.notice.domain.notice.UploadFile;
import com.rest.notice.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service @Transactional
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final FileNotice fileNotice;

    @Override
    public String saveNotice(NoticeRequest request,List<MultipartFile> files) {
        // Todo: IOException 처리방법 알아보기 수정해야 할 부분
        try {
            List<UploadFile> uploadFiles = fileNotice.repositoryFile(files);
            noticeRepository.save(Notice.createNotice(request.getTitle(), request.getContent(), request.getWriter(), request.getEndDate(),uploadFiles));
        }catch (IOException e){
            e.printStackTrace();
            // throw new RuntimeException( FileUploadException ... custom??)
        }
        return null;
    }

    @Override
    public String modifyNotice(Long noticeId, NoticeRequest request, List<MultipartFile> files) {
        // Todo: Try-Catch fileNotice 안으로 옮겨서 한번에 ( 런타임 익셉션으로 변경해서 날려줘 )
        try {
            Notice notice = noticeRepository.findByNotice(noticeId);

            // 현재 저장 파일 전체삭제
            notice.getUploadFiles().forEach(uploadFile -> fileNotice.deleteFile(uploadFile.getRepositoryFileName()));
            notice.setUpdate(request, fileNotice.repositoryFile(files));
            return null;
        }catch (IOException e){
        e.printStackTrace();
        // throw new RuntimeException( FileUploadException ... custom??)
    }
        return null;
    }

    @Override
    public String deleteNotice(Long noticeId) {
        noticeRepository.deleteNotice(noticeRepository.findByNotice(noticeId));
        return null;
    }
}
