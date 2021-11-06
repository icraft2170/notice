package com.rest.notice.repository.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class NoticeRepositoryImpl implements NoticeRepository{

    private final EntityManager em;

}
