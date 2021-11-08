package com.rest.notice.api.notice;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rest.notice.api.notice.request.NoticeRequest;
import com.rest.notice.api.notice.response.CreateNoticeResponse;
import com.rest.notice.api.notice.response.DeleteNoticeResponse;
import com.rest.notice.api.notice.response.ModifyNoticeResponse;
import com.rest.notice.dto.NoticeQueryDto;

import com.rest.notice.dto.Page;
import com.rest.notice.dto.Pageable;
import com.rest.notice.dto.Result;
import com.rest.notice.service.notice.NoticeQueryServiceImpl;
import com.rest.notice.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final ObjectMapper objectMapper;

    @PostMapping("/new")
    public CreateNoticeResponse createNotice(
                              @RequestPart(name = "content") String content,
                              @RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        NoticeRequest request = objectMapper.readValue(content, NoticeRequest.class);
        noticeService.saveNotice(request,files);
        return new CreateNoticeResponse("ok");
    }

    @PutMapping("/{noticeId}/update")
    public ModifyNoticeResponse modifyNotice(
            @PathVariable Long noticeId
            ,@RequestPart(name = "content") String content
            ,@RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        NoticeRequest request = objectMapper.readValue(content, NoticeRequest.class);
        noticeService.modifyNotice(noticeId,request,files);
        return new ModifyNoticeResponse("ok");
    }

    @DeleteMapping("/{noticeId}/delete")
    public DeleteNoticeResponse deleteNotice(@PathVariable Long noticeId){
        noticeService.deleteNotice(noticeId);
        return new DeleteNoticeResponse("ok");
    }

    @GetMapping
    public Page<NoticeQueryDto> notices(@ModelAttribute Pageable pageable) {
        return noticeQueryService.findAllNotice(pageable);
    }

    @GetMapping("/{noticeId}")
    public Result notice(@PathVariable Long noticeId){
//        NoticeDto noticeDto = noticeQueryService.findOneNotice;
        return null;
    }

}
