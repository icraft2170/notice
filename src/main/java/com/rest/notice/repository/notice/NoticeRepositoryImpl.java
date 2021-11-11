package com.rest.notice.repository.notice;

import com.rest.notice.dto.NoticesQueryDto;
import com.rest.notice.domain.notice.Notice;
import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;
import com.rest.notice.exception.NoticeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
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
        try {
            return em.createQuery("select n" +
                            " from Notice n" +
                            " join fetch n.uploadFiles" +
                            " where n.id = :noticeId", Notice.class)
                    .setParameter("noticeId", noticeId)
                    .getSingleResult();
        }catch (PersistenceException e){
            throw new NoticeException("공지사항 검색 실패");
        }
    }

    @Override
    public void deleteNotice(Notice notice) {
        em.remove(notice);
    }

    @Override
    public Page<NoticesQueryDto> findPageNotice(Pageable pageable) {
        List<Notice> contents = em.createQuery("select n" +
                        " from Notice n" +
                        " order by n.registrationDate DESC", Notice.class)
                .setFirstResult(pageable.getStartNum())
                .setMaxResults(pageable.getLastNum())
                .getResultList();
        log.debug("startNo = {} , lastNo = {}", pageable.getStartNum(), pageable.getLastNum());


        List counts = em.createQuery("select count(n) from Notice n").getResultList();
        return new Page<NoticesQueryDto>(pageable.getPageNumber(),pageable.getPageSize(), Integer.parseInt(String.valueOf(counts.get(0))),contents.stream().map(NoticesQueryDto::new).collect(Collectors.toList()));
    }

    @Override
    public Notice findOne(Long noticeId) {
        try {
            return em.createQuery("select n" +
                            " from Notice n" +
                            " where n.id = :noticeId", Notice.class)
                    .setParameter("noticeId", noticeId)
                    .getSingleResult();
        }catch (PersistenceException e){
            throw new NoticeException("공지사항 검색 실패");
        }
    }

    @Override
    public List<Notice> findAll() {
        return em.createQuery("select n from Notice n",Notice.class)
                .getResultList();
    }


}
