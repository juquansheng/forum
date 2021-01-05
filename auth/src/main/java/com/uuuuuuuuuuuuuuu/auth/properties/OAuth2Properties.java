package com.uuuuuuuuuuuuuuu.auth.properties;

import lombok.Data;


@Data
public class OAuth2Properties {

    private String jwtSigningKey = "yuuki";

    private OAuth2ClientProperties[] clients = {};
}
