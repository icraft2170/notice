

## 이슈 1

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