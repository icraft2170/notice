package com.rest.notice.api.notice;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.notice.domain.notice.Notice;
import com.rest.notice.dto.NoticeQueryDto;
import com.rest.notice.dto.Result;
import com.rest.notice.repository.notice.NoticeRepository;
import com.rest.notice.service.notice.NoticeQueryService;
import com.rest.notice.service.notice.NoticeService;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Part;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoticeApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }




    @Test
    @DisplayName(value = "공지사항 등록")
    void createNotice() throws Exception {
        //given
        String title = "new-notice";
        String url = "http://localhost:" + port +"/notice/post";
        String content = "{ \"title\":\"" + title + "\",\"content\":\"notice-new-content\",\"writer\" : \"hyeonho\",\"endDate\":\"2022-03-04T12:00:00\" }";

        String fileUuid = UUID.randomUUID().toString();
        MockMultipartFile file
                = new MockMultipartFile(
                "test.txt",
                "/Users/sonhyeonho/Desktop/rest/notice/src/main/resources/file/" + fileUuid +".txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Test File!".getBytes());

        //when
        List<Notice> first = noticeRepository.findAll();
        assertThat(first.size()).isEqualTo(0);
        mockMvc.perform(multipart(url)
                        .file("files",file.getBytes())
                        .part(
                                new MockPart("content",content.getBytes(StandardCharsets.UTF_8))
                        ))
                .andExpect(status().isOk());

        //then
        List<Notice> second = noticeRepository.findAll();
        assertThat(second.size()).isEqualTo(1);

        Notice findNotice = noticeRepository.findByNotice(1L);
        assertThat(findNotice.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName(value = "공지사항 수정")
    void modifyNotice() throws Exception {
        //given
        mockMvc.perform(multipart("http://localhost:" + port +"/notice/post")
                        .file("files", new MockMultipartFile(
                                "test.txt",
                                "/Users/sonhyeonho/Desktop/rest/notice/src/main/resources/file/" + UUID.randomUUID().toString() +".txt",
                                MediaType.TEXT_PLAIN_VALUE,
                                "Test File!".getBytes())
                                .getBytes())
                        .part(
                                new MockPart("content", ("{ \"title\":\"" + "new-notice" + "\",\"content\":\"notice-new-content\",\"writer\" : \"hyeonho\",\"endDate\":\"2022-03-04T12:00:00\" }").getBytes(StandardCharsets.UTF_8))
                        ))
                .andExpect(status().isOk());

        String title = "modify-notice";
        String url = "http://localhost:" + port + "/notice/1/post";
        String content = "{\"title\":\""+ title +"\",\"content\":\"notice-modify-content\",\"writer\" : \"modifyhyeonho\",\"endDate\":\"2022-12-04T12:00:00\"}";


        String fileUuid = UUID.randomUUID().toString();
        MockMultipartFile file
                = new MockMultipartFile(
                "modifyTest.txt",
                "/Users/sonhyeonho/Desktop/rest/notice/src/main/resources/file/" + fileUuid +".txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Modify File!".getBytes());

        //when

        mockMvc.perform(multipart(url)
                        .file("files",file.getBytes())
                        .part(
                                new MockPart("content",content.getBytes(StandardCharsets.UTF_8))
                        ))
                .andExpect(status().isOk());

        //then
        Notice findNotice = noticeRepository.findByNotice(1L);
        assertThat(findNotice.getTitle()).isEqualTo(title);
        assertThat(findNotice.getTitle()).isNotEqualTo("new-notice");
    }

    @Test
    @DisplayName(value = "공지사항 삭제")
    void deleteNotice() throws Exception {
        //given
        mockMvc.perform(multipart("http://localhost:" + port +"/notice/post")
                        .file("files", new MockMultipartFile(
                                "test.txt",
                                "/Users/sonhyeonho/Desktop/rest/notice/src/main/resources/file/" + UUID.randomUUID().toString() +".txt",
                                MediaType.TEXT_PLAIN_VALUE,
                                "Test File!".getBytes())
                                .getBytes())
                        .part(
                                new MockPart("content", ("{ \"title\":\"" + "new-notice" + "\",\"content\":\"notice-new-content\",\"writer\" : \"hyeonho\",\"endDate\":\"2022-03-04T12:00:00\" }").getBytes(StandardCharsets.UTF_8))
                        ))
                .andExpect(status().isOk());
        String url = "http://localhost:" + port + "/notice/1/delete";

        //when
        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        //then
        List<Notice> notices = new ArrayList<>();
        notices = noticeRepository.findAll();
        assertThat(notices.size()).isEqualTo(0);
    }


    @Test
    @DisplayName(value = "공지사항 상세조회")
    void notice() throws Exception {
        //given

        String title = "new-notice";
        String saveUrl = "http://localhost:" + port +"/notice/post";
        String url = "http://localhost:" + port + "/notice/1/get";
        String json = "{ \"title\":\"" + title + "\",\"content\":\"notice-new-content\",\"writer\" : \"hyeonho\",\"endDate\":\"2022-03-04T12:00:00\" }";
        String fileUuid = UUID.randomUUID().toString();
        MockMultipartFile file
                = new MockMultipartFile(
                "test.txt",
                "/Users/sonhyeonho/Desktop/rest/notice/src/main/resources/file/" + fileUuid +".txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Test File!".getBytes());

        mockMvc.perform(multipart(saveUrl)
                        .file("files",file.getBytes())
                        .part(
                                new MockPart("content",json.getBytes(StandardCharsets.UTF_8))
                        ))
                .andExpect(status().isOk());


        //when
        MvcResult mvcResult = mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        //Todo: 블로그 만들면 정리해두기. [JavaType 클래스로 제네릭 표현하기]
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Result.class, NoticeQueryDto.class);
        Result<NoticeQueryDto> result = objectMapper.readValue(content, type);

        NoticeQueryDto data = result.getData();
        //then
        assertThat(data.getTitle()).isEqualTo(title);
    }




}

