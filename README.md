# Notice Rest API

[주제]
공지사항 관리 REST API 구현

[기능 요구사항]
- 공지사항 등록, 수정, 삭제, 조회 API를 구현.
- 공지사항 등록시 입력 항목은 다음과 같다
  - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개) 
- 공지사항 조회시 응답은 다음과 같다.
  - 제목, 내용, 등록일시, 조회수, 작성자

## 1. 실행 방법 

### 의존성
```groovy
plugins {
	id 'org.springframework.boot' version '2.5.6'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
}

group = 'com.rest'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}
```
#### Lombok 설정
- InteliJ
  - https://leeys.tistory.com/27
- STS
  -  https://the-dev.tistory.com/27

#### H2 DataBase Download
- https://programmer93.tistory.com/61

## 실행 확인 방법 

### PostMan 사용
- https://www.postman.com/

## 2. 테스트 코드
- Junit5 , AssertJ , Mock 사용
-  `NoticeRepositoryTest` 단위 테스트 작성
-  `NoticeApiControllerTest` 통합 테스트 작성


## 3. DataBase

### (MySQL 기준) DDL 예시 
- 실행은 H2 DataBase로  `application.properties`에서 `auto-ddl: create`(Mode)로 진행하여야 합니다.
```mysql
CREATE TABLE notice
(
    notice_id         BIGINT       NOT NULL,
    title             VARCHAR(40) NOT NULL,
    content           LONGTEXT     NOT NULL,
    registration_date datetime     NOT NULL,
    end_date          datetime     NOT NULL,
    hit               INT          NOT NULL,
    writer            VARCHAR(30) NOT NULL,
    CONSTRAINT pk_notice PRIMARY KEY (notice_id)
);
```

```mysql
CREATE TABLE upload_files
(
    upload_file_id       BIGINT       NOT NULL,
    upload_file_name     VARCHAR(40) NOT NULL,
    repository_file_name VARCHAR(50) NOT NULL,
    notice_id            BIGINT       NOT NULL,
    CONSTRAINT pk_upload_files PRIMARY KEY (upload_file_id)
);

ALTER TABLE upload_files
    ADD CONSTRAINT FK_UPLOAD_FILES_ON_NOTICE FOREIGN KEY (notice_id) REFERENCES notice (notice_id);
```


### Table 명세

- NOTICE 
    - NOTICE_ID (Primary Key)
    - TITLE (NOT NULL)
    - CONTENT (NOT NULL)
    - WRITER (NOT NULL)
    - HIT (NOT NULL)
    - REGISTRATION_DATE (NOT NULL)
    - END_DATE (NOT NULL)
     
- UPLOAD_FILES
  - UPLOAD_FILE_ID (Primary Key)
  - REPOSITORY_FILE_NAME (UNIQUE, NOT NULL)
  - UPLOAD_FILE_NAME (NOT NULL)
  - NOTICE_ID (Foreign Key)

[comment]: <> (### ERD)

[comment]: <> (![Erd]&#40;./images/Notice.png&#41;)




## 4. Response Method

|URL|메서드명|리턴|전송 방식|기능|
|-------------|---------------|----------------|--------------------|---------------|
|/notice/post|createNotice|ResponseEntity|POST|Notice 등록|
|/notice/{noticeId}/post|modifyNotice|ResponseEntity|Post|Notice 수정|
|/notice/{noticeId}/delete|deleteNotice|ResponseEntity|DELETE|Notice 삭제|
|/notice/gets|notices|Page<NoticesQueryDto>|GET|Notice List|
|/notice/{noticeId}/get|notice|Result|GET|Notice 상세 조회|


# 5. 문제 상황과 해결 과정

## 이슈 1
### 입력 과정에서 Json 형태의 (Notice data)와 File을 같이 받아와야 하는 경우.

1. 처음에는 `@RequestBody`를 통해서 Json을 받아오고 MultipartFile을 따로 받을 수 있을 것이라 판단하였으나. MultiPart형식은 결국
Http Body에 구분을 지어서 여러 파트를 나누어 보내는 것이기 때문에 RequestBody를 통해 따로 JSON만 받아오는 것은 불가능한 문제.
```java
// 불가능 
    @PostMapping("/new")
    public String writeNotice(@RequestBody NoticeRequest request,
                                      List<MultipartFile> files){
        noticeService.saveNotice(request,files);
        return "ok";
    }
```
2. 해당 방법을 해결하기 위해 @RequestPart를 통해 각 Part를 받아오고 JSON은 String형으로 받아와 ObjectMapper를 통해 Object로 변경 하였다.
```java
@PostMapping("/new")
    public CreateNoticeResponse createNotice(
                              @RequestPart(name = "content") String content,
                              @RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        NoticeRequest request = objectMapper.readValue(content, NoticeRequest.class);
        noticeService.saveNotice(request,files);
        return new CreateNoticeResponse("ok");
    }
```


## 이슈 2
### 등록 및 수정 과정에서 Json을 Object로 변경할 때 DateFormat문제 발생
>com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling 

1. ObjectMapper를 사용함에 따라. Date Format 문제 발생. `Spring Boot 2.xxx`부터는 jackson-jsr-310이 기본적으로 포함됨에도 
DateFormat 오류 발생

```java

@Data
public class NoticeRequest {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String writer;

//  @JsonFormat(....)
//  @DateTimeFormat(....)
    private LocalDateTime endDate;
}

```
2. `@JsonFormat` , `@DateTimeFormat`을 통해 해결을 시도했으나. 같은 문제가 지속적으로 발생하였다.
   - ObjectMapper는 DTO에 위와 같은 Annotation을 붙이는 것으로는 해결이 불가능한 것을 확인.

```java
@Configuration
public class JsonConfig {

    @Bean("objectMapper")
    public ObjectMapper objectMapper(){
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }
}
```

3. ObjectMapper Bean 설정 변경하여 등록. 이후, Jackson의 DateFormat을 변경해주고, DI 받는 방법을 선택. 
   - 서버 환경에서 다수에 ObjectMapper를 생성할 필요 없어지고 싱글톤으로 관리할 수 있도록 하였다.
    

## 이슈 3
### 파일 업로드과정에서 IOException(체크예외)이 계속 던져져 `FileNotice`를 사용하는 모든 곳에 해당 체크예외가 `throw`됨
### 파일 업로드 과정에서 예외가 발생했을 때 IOException으로 던져져 정확한 원인 파악이 힘들다는 문제.


```java

    public UploadFile storeFile(MultipartFile multipartFile)
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        try{
            String originalFilename = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);
            // 파일 저장
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
            return UploadFile.createUploadFile(originalFilename, storeFileName);
        }catch (IOException e){
            throw new FileUploadFailException("파일 저장 실패 예외", e);
        }
    }
```
- 해당 문제 해결을 위해 IOException 발생 가능성이 있는 `storeFile`에서 예외처리를 해주고 Custom Exception인 `FileUploadFailException`
으로 예외 전환을 해준다.   `FileUploadFailException`는 `RuntimeException`을 상속받아 언체크 예외로 예외처리 필수가 아니기 때문에 
`FileNotice`를 사용하는 다른 곳에서 해당 예외처리를 코드로 처리할 필요가 사라졌다.

## 이슈 4
### 페이징 처리를 위한 반환 형태, 및 Api 스펙변경에 유연한 구조

```java

public class Page <T>{
    private Integer pageNumber;
    private Integer size;
    private Integer totalCount;
    private List<T> contents = new ArrayList<>();

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<T> getContents() {
        return contents;
    }


    public Page(Integer pageNumber, Integer size, Integer totalCount, List<T> contents) {
        this.pageNumber = pageNumber;
        this.size = size;
        this.totalCount = totalCount;
        this.contents = contents;
    }

    public Page(List<T> contents,int totalCount) {
        this(0,10,totalCount,contents);
    }
}
```
- Spring에서 제공해주는 `Page` 혹은 `Pageable` 을 참고하여 해당 api의 dto를 감싸는 클래스를 생성하고 해당 클래스에 페이징에 필요한
데이터를 추가해서 보낼 수 있는 환경 제공

## 이슈 5
### MultiPart Put -> Post로 변경

- 최초에 Notice 수정 Api Controller는 Put으로 진행하였으나. 통합 테스트 작성 과정에서 `Mock`에서 Put으로는 MultiPart 지원이 불가능하였다.
이에 좀더 알아보니, 스프링 웹 MVC는 해당 내용을 Post로만 인식한다. 이유는 `Tomcat`에 `ServletFileUpload`가 아래 코드와 같이 하드코딩 되어있기 때문이다.
`multipart/form-data` 는 현재 Post로 해야하기 때문에 Put으로 작성한 내용 변경 
```java
    public static final boolean isMultipartContent(
            final HttpServletRequest request) {
        if (!POST_METHOD.equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        return FileUploadBase.isMultipartContent(new ServletRequestContext(request));
    }
/**
 * Spring Boot 내장 (apache.tomcat.embed) Tocat 9.0.54 ( Spring Boot 2.5.6 Version ) 기준 
 */
```
> 출처 : https://blog.outsider.ne.kr/1001 

## 이슈 6
### Controller Mapping Url 변경
- 최초 작성에 Mapping url이 Restful 형식과 맞지않아 해당 규약에 대해 구글링을 통해 변경하였다.

[comment]: <> (## 이슈 8)

[comment]: <> (### Test &#40; auto-ddl : create-drop &#41;)

[comment]: <> (여유될 때 추가하자 정리하자)

## 대용량 트래픽 대응 노력
- ObjectMapper Bean 등록하여 DI를 통해 사용
- 페치 조인
- 각각의 도메인에 스스로와 관련된 비지니스 로직을 응집하면서. 유지보수가 편리해짐.
