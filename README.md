

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
