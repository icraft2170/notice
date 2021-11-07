package com.rest.notice.repository.notice;

import com.rest.notice.domain.notice.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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
}
