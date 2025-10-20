package com.newscheck.newscheck.models.requests;

import com.newscheck.newscheck.models.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequestDTO {

    private Long userId;
    private ContentType contentType;
    private String contentText;
    private String contentUrl;
    private String imageBase64;

}
