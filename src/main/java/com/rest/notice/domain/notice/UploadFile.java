package com.rest.notice.domain.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "UPLOAD_FILES")
public class UploadFile {
    @Id @GeneratedValue
    @Column(name = "upload_file_id")
    private Long id;

    @Column(name = "upload_file_name")
    private String uploadFileName;

    @Column(name = "repository_file_name")
    private String repositoryFileName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;
}
