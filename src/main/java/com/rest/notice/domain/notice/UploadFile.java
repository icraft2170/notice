package com.rest.notice.domain.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Entity
@Table(name = "UPLOAD_FILES")
@SequenceGenerator(
        name="FILE_SEQ_GEN",
        sequenceName="FILE_SEQ",
        initialValue=1,
        allocationSize=100)
public class UploadFile {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ_GEN")
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


    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public static UploadFile createUploadFile(String uploadFileName, String repositoryFileName) {
        return new UploadFile(uploadFileName,repositoryFileName);
    }

    private UploadFile(String uploadFileName, String repositoryFileName) {
        this.uploadFileName = uploadFileName;
        this.repositoryFileName = repositoryFileName;
    }

    public void updateUploadFile(String uploadFileName, String repositoryFileName){
        this.uploadFileName = uploadFileName;
        this.repositoryFileName = repositoryFileName;
    }


}
