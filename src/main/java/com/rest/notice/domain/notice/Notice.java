package com.rest.notice.domain.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "NOTICE")
public class Notice {
    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    @Column(name = "notice_title")
    private String title;

    @Lob
    @Column(name = "notice_content")
    private String content;

    @Column(name = "notice_registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "notice_end_date")
    private LocalDateTime endDate;

    @Column(name = "notice_hit")
    private Integer hit;

    @Column(name = "notice_writer")
    private String writer;

    @OneToMany(mappedBy = "notice")
    private List<UploadFile> uploadFiles = new ArrayList<>();
}
