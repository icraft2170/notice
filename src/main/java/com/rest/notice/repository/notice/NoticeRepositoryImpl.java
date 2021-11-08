package com.rest.notice.repository.notice;

import com.rest.notice.dto.NoticeQueryDto;
import com.rest.notice.domain.notice.Notice;
import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class NoticeRepositoryImpl implements NoticeRepository{
    private final EntityManager em;

    @Override
    public void save(Notice notice) {
        em.persist(notice);
    }

    @Override
    public Notice findByNotice(Long noticeId) {
        // fetchJoin
        return em.createQuery("select n" +
                        " from Notice n" +
                        " join fetch n.uploadFiles" +
                        " where n.id = :noticeId", Notice.class)
                .setParameter("noticeId", noticeId)
                .getResultList().get(0);
    }

    @Override
    public void deleteNotice(Notice notice) {
        em.remove(notice);
    }

    @Override
    public Page<NoticeQueryDto> findPageNotice(Pageable pageable) {
        List<Notice> contents = em.createQuery("select n" +
                        " from Notice n" +
                        " order by n.registrationDate DESC", Notice.class)
                .setFirstResult(pageable.getStartNum())
                .setMaxResults(pageable.getLastNum())
                .getResultList();


        List counts = em.createQuery("select count(n) from Notice n").getResultList();
        return new Page<NoticeQueryDto>(pageable.getPageNumber(),pageable.getPageSize(), Integer.parseInt(String.valueOf(counts.get(0))),contents.stream().map(NoticeQueryDto::new).collect(Collectors.toList()));
    }
}
