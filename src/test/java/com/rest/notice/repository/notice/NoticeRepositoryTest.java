package com.rest.notice.repository.notice;

import com.rest.notice.domain.notice.Notice;
import com.rest.notice.domain.notice.UploadFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;


import com.rest.notice.repository.notice.NoticeRepository;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application-test.properties")
@DataJpaTest
class NoticeRepositoryTest {
// Todo: https://www.inflearn.com/questions/23063 (DataJpaTest의 빈등록 : 정리해두기)
// Todo: https://stackoverflow.com/questions/39807483/sequence-hibernate-sequence-not-found-sql-statement (이유 찾기 시퀀스 전략에 Create-drop으로 해야하는 : 정리해두기)

    @Autowired
    EntityManager em;
    NoticeRepository noticeRepository;


    @BeforeEach
    void setUp(){
        noticeRepository = new NoticeRepositoryImpl(em);

    }

    @Test
    void save() {
        UploadFile file1 = new UploadFile(null, "file1", "abc-123-456", null);
        UploadFile file2 = new UploadFile(null, "file2", "fgh-456-789", null);
        List<UploadFile> uploadFiles = Arrays.asList(file1, file2);

        Notice notice = Notice.createNotice("공지사항", "공지사항 컨텐츠", "손현호",
                LocalDateTime.now().plusDays(3L), uploadFiles);
        //when
        noticeRepository.save(notice);
        Notice saveNotice = noticeRepository.findByNotice(notice.getId());
        //then
        assertThat(saveNotice).isEqualTo(notice);
    }
    @Test
    void saveV2() {
        UploadFile file1 = new UploadFile(null, "file1", "abc-123-456", null);
        UploadFile file2 = new UploadFile(null, "file2", "fgh-456-789", null);
        List<UploadFile> uploadFiles = Arrays.asList(file1, file2);

        Notice notice = new Notice(null, "공지사항", "공지사항 컨텐츠", LocalDateTime.now(), LocalDateTime.now().plusDays(1L), 0, "손현호", null);
        //when
        noticeRepository.save(notice);
        Notice saveNotice = noticeRepository.findOne(notice.getId());
        //then
        assertThat(saveNotice).isEqualTo(notice);
    }
    @Test
    void negativeSaveV1() {
        UploadFile file1 = new UploadFile(null, "file1", "abc-123-456", null);
        UploadFile file2 = new UploadFile(null, "file2", "fgh-456-789", null);
        List<UploadFile> uploadFiles = Arrays.asList(file1, file2);
        //when
        Notice notice = null;
        //then
        assertThrows(IllegalArgumentException.class,()->noticeRepository.save(notice));
    }


    @Test
    void deleteNotice() {
        UploadFile file1 = new UploadFile(null, "file1", "abc-123-456", null);
        UploadFile file2 = new UploadFile(null, "file2", "fgh-456-789", null);
        List<UploadFile> uploadFiles = Arrays.asList(file1, file2);

        Notice notice = Notice.createNotice("공지사항", "공지사항 컨텐츠", "손현호",
                LocalDateTime.now().plusDays(3L), uploadFiles);
        //when
        noticeRepository.save(notice);
        List<Notice> firstFind = noticeRepository.findAll();
        assertThat(firstFind.size()).isEqualTo(1);
        noticeRepository.deleteNotice(notice);
        //then
        List<Notice> secondFind = noticeRepository.findAll();
        assertThat(secondFind.size()).isEqualTo(0);
    }

    @Test
    void findOne(){
        //given
        UploadFile file1 = new UploadFile(null, "file1", "abc-123-456", null);
        UploadFile file2 = new UploadFile(null, "file2", "fgh-456-789", null);
        List<UploadFile> uploadFiles = Arrays.asList(file1, file2);
        Notice notice = Notice.createNotice("공지사항", "공지사항 컨텐츠", "손현호",
                LocalDateTime.now().plusDays(3L), uploadFiles);
        Notice notice2 = Notice.createNotice("공지사항2", "공지사항 컨텐츠2", "손현호2",
                LocalDateTime.now().plusDays(2L), uploadFiles);
        //when
        noticeRepository.save(notice);
        noticeRepository.save(notice2);
        Notice findNotice = noticeRepository.findOne(notice.getId());
        //then
        assertThat(notice).isEqualTo(findNotice);
        assertThat(notice2).isNotEqualTo(findNotice);
    }
}