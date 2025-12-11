package com.ktb.chatapp.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileMetadataRequest {
    @JsonProperty("_id")
    private String id;
    private String filename;
    private String originalname;
    private String mimeType;
    private long size;
}