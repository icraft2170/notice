package com.rest.notice.api.notice.file;

import com.rest.notice.domain.notice.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileNotice {

    @Value("${file.dir}")
    private String fileDir;


    public List<UploadFile> repositoryFile(List<MultipartFile> multipartFiles) throws IOException{
        List<UploadFile> results = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                results.add(storeFile(multipartFile));
            }
        }
        return results;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        // 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return UploadFile.createUploadFile(originalFilename, storeFileName);
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }
}



