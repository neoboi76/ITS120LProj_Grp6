package com.newscheck.newscheck.models.responses;

import com.newscheck.newscheck.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
    private String language;
    private Role role;
    private LocalDateTime createdAt;
    private Long totalVerifications;
    private Long totalAuditLogs;

}