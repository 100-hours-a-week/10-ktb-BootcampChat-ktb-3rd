package com.ktb.chatapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktb.chatapp.model.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {

    @JsonProperty("_id")
    private String id;             // 파일 uuid
    private String filename;
    private String originalname;
    private String mimeType;
    private long size;
    private String url;

    // File 엔티티에서 FileResponse로 변환하는 정적 메서드
    public static FileResponse from(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .filename(file.getFilename())
                .originalname(file.getOriginalname())
                .mimeType(file.getMimetype())
                .size(file.getSize())
                .build();
    }
}
