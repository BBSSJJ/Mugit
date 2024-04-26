package com.ssafy.mugit.global.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleUserInfoDto {
    private String id;
    private String email;
    @JsonProperty("verified_email")
    private String verifiedEmail;
    private String name;
    @JsonProperty("given_name")
    private String givenName;
    private String picture;
}
