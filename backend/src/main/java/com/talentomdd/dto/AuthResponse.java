package com.talentomdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserSummary user;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String role;
    }
}
