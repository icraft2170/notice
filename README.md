# Notice Rest API

[주제]
공지사항 관리 REST API 구현

[기능 요구사항]
- 공지사항 등록, 수정, 삭제, 조회 API를 구현.
- 공지사항 등록시 입력 항목은 다음과 같다
  - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개) 
- 공지사항 조회시 응답은 다음과 같다.
  - 제목, 내용, 등록일시, 조회수, 작성자

## 실행 방법 

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
####  Lombok 설정
- InteliJ
  - https://leeys.tistory.com/27
- STS
  -  https://the-dev.tistory.com/27

#### H2 DataBase Download
- https://programmer93.tistory.com/61

## 실행 확인 방법 

### PostMan 사용
- https://www.postman.com/

### 테스트 코드
// 작성 예정


## DataBase

### Table 명세

- NOTICE 
    - NOTICE_ID (Primary Key)
    - NOTICE_TITLE (NOT NULL)
    - NOTICE_CONTENT (NOT NULL)
    - NOTICE_WRITER (NOT NULL)
    - NOTICE_HIT (NOT NULL)
    - NOTICE_REGISTRATION_DATE (NOT NULL)
    - NOTICE_END_DATE (NOT NULL)
     
- UPLOAD_FILES
  - UPLOAD_FILE_ID (Primary Key)
  - REPOSITORY_FILE_NAME (UNIQUE, NOT NULL)
  - UPLOAD_FILE_NAME (NOT NULL)
  - NOTICE_ID (Foreign Key)

### ERD

// 추가 예정


### Response Method

|URL|메서드명|전송 방식|기능|
|-------------|---------------|--------------------|---------------|
|/notice/new|createNotice|POST|Notice 등록|
|/notice/{noticeId}/update|modifyNotice|PUT|Notice 수정|
|/notice/{noticeId}/delete|deleteNotice|DELETE|Notice 삭제|
|/notice|notices|GET|Notice List|
|/notice/{noticeId}|notice|GET|Notice 상세 조회|


## 이슈 1

- 파일을 

```java
    @PostMapping("/new")
    public String writeNotice(@RequestBody NoticeRequest request,
                                      List<MultipartFile> files){
        log.info("title: {}, content: {}, writer : {}",request.getTitle(),request.getContent(),request.getWriter());
        noticeService.saveNotice(request,files);
        return "ok";
    }
```
- RequestBody와 MultipartFile가 같이 작동하지 않음  null,null,null


```java
    @PostMapping("/new")
    public ResponseEntity writeNotice(
                              @RequestPart(name = "content") String content,
                              @RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        NoticeRequest request = new ObjectMapper().readValue(content, NoticeRequest.class);
        noticeService.saveNotice(request,files);
        return null;
    }
```

- MultiPart에서는 RequestBody를 지원할 수 없는 구조이기 때문에 String 형태로 Json을 받아서 ObjectMapper를 통해 객체로 변환


## 이슈 2
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
at [Source: (String)"{"title":"new-notice","content":"notice-new-content","writer" : "hyeonho", "endDate":"2022-03-04T12:21:03"}"; line: 1, column: 86] (through reference chain: com.rest.notice.api.notice.request.NoticeRequest["endDate"])

https://itpro.tistory.com/117

-
ObjectMapper 포맷 전환 

- DI 까지

... DateFormat 추가 설명

## 이슈 3
FileNotice에서 예외전환 (IOException -> FileUploadFailException ( Runtime Exception )


## 이슈 4
- Page Dto로 감싸서 전달....