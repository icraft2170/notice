package com.rest.notice.api.notice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.notice.api.notice.request.NoticeRequest;
import com.rest.notice.api.notice.response.NoticeResponse;
import com.rest.notice.dto.*;

import com.rest.notice.service.notice.NoticeQueryService;
import com.rest.notice.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
public class NoticeApiController {
    private final NoticeService noticeService;
    private final NoticeQueryService noticeQueryService;
    private final ObjectMapper objectMapper;



    @PostMapping("/post")
    public ResponseEntity<String> createNotice(
                              @RequestPart(name = "content") String content,
                              @RequestPart(value = "files",required = false) List<MultipartFile> files) throws JsonProcessingException {

            NoticeRequest request = objectMapper.readValue(content, NoticeRequest.class);
            noticeService.saveNotice(request,files);

            return ResponseEntity
                    .created(URI.create("/notice/post"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(
                            NoticeResponse.builder()
                                    .message("create success")
                                    .build()
                    ));
    }



    @PostMapping("/{noticeId}/post")
    public ResponseEntity<String> modifyNotice(
            @PathVariable Long noticeId
            ,@RequestPart(name = "content") String content
            ,@RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {

           NoticeRequest request = objectMapper.readValue(content, NoticeRequest.class);
           noticeService.modifyNotice(noticeId,request,files);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(
                        NoticeResponse.builder()
                                .message("modify success")
                                .build()
                ));
    }

    @DeleteMapping("/{noticeId}/delete")
    public ResponseEntity<String> deleteNotice(@PathVariable Long noticeId) throws JsonProcessingException {
            noticeService.deleteNotice(noticeId);

            return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(
                            NoticeResponse.builder()
                                    .message("delete success")
                                    .build()
                    ));
    }


    @GetMapping("/gets")
    public Page<NoticesQueryDto> notices(@ModelAttribute Pageable pageable) {
        return noticeQueryService.findAllNotice(pageable);
    }

    @GetMapping("/{noticeId}/get")
    public Result notice(@PathVariable Long noticeId){
        return new Result<NoticeQueryDto>(noticeQueryService.findOneNotice(noticeId));
    }

}
