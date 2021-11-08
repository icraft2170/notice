package com.rest.notice.domain.notice;

import com.rest.notice.api.notice.request.NoticeRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "NOTICE")
@SequenceGenerator(
        name="NOTICE_SEQ_GEN",
        sequenceName="NOTICE_SEQ",
        initialValue=1,
        allocationSize=100)
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTICE_SEQ_GEN")
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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    // 생성 메서드
    public static Notice createNotice(String title,String content,String writer,LocalDateTime endDate ,List<UploadFile> uploadFiles){
        Notice notice = new Notice(null, title, content, LocalDateTime.now(), endDate, 0, writer, null);
        notice.setUploadFiles(uploadFiles);
        return notice;
    }

    // 연관관계 메서드
    private void setUploadFiles(List<UploadFile> uploadFiles) {
        this.uploadFiles = uploadFiles;
        uploadFiles.forEach(uploadFile -> uploadFile.setNotice(this));
    }


    public void setUpdate(NoticeRequest request, List<UploadFile> uploadFiles) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.writer = request.getWriter();
        this.endDate = request.getEndDate();
        for (int i = 0; i < uploadFiles.size(); i++) {
            if(i < this.uploadFiles.size()) {
                UploadFile uploadFile = this.uploadFiles.get(i);
                UploadFile changeUploadFile = uploadFiles.get(i);

                uploadFile.updateUploadFile(changeUploadFile.getUploadFileName(),changeUploadFile.getRepositoryFileName());
            }else{
                uploadFiles.get(i).setNotice(this);
                this.uploadFiles.add(uploadFiles.get(i));
            }
        }
    }
}
