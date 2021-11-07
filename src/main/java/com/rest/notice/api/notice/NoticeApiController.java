package com.rest.notice.api.notice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.notice.api.notice.request.NoticeRequest;
import com.rest.notice.api.notice.response.MsgNoticeResponse;
import com.rest.notice.api.notice.response.NoticeResponse;
import com.rest.notice.service.notice.NoticeQueryServiceImpl;
import com.rest.notice.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
public class NoticeApiController {
    private final NoticeService noticeService;
    private final NoticeQueryServiceImpl noticeQueryService;

    @PostMapping("/new")
    public ResponseEntity writeNotice(
                              @RequestPart(name = "content") String content,
                              @RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        NoticeRequest request = new ObjectMapper().readValue(content, NoticeRequest.class);
        noticeService.saveNotice(request,files);
        return null;
    }


    @PutMapping("/{noticeId}/update")
    public ResponseEntity modifyNotice(
            @PathVariable Long noticeId
            ,@RequestPart(name = "content") String content
            ,@RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        NoticeRequest request = new ObjectMapper().readValue(content, NoticeRequest.class);
        noticeService.modifyNotice(noticeId,request,files);
        return null;
    }



    @DeleteMapping("/{noticeId}/delete")
    public MsgNoticeResponse deleteNotice(@PathVariable Long noticeId){

        String msg = noticeService.deleteNotice(noticeId);
        return new MsgNoticeResponse(msg);
    }

    @GetMapping
    public List<NoticeResponse> notices(){
        return noticeQueryService.findAllNotice();
    }


}
