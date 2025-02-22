package com.erp.erp.domain.auth.business;

import io.jsonwebtoken.io.Decoders;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class TokenProperties {

    private final String ALGORITHM = "HmacSHA256";

    private Access access;
    private Refresh refresh;


    @Getter
    @Setter
    public static class Access {
        private String secret;
        private Long exp;
    }

    @Getter
    @Setter
    public static class Refresh {
        private String secret;
        private Long exp;
    }

    public SecretKey getAccessSecretKey() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(access.secret);
        return new SecretKeySpec(accessKeyBytes, ALGORITHM);
    }

    public SecretKey getRefreshSecretKey() {
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refresh.secret);
        return new SecretKeySpec(refreshKeyBytes, ALGORITHM);
    }
}
