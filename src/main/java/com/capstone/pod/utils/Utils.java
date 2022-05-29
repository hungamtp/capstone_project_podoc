package com.capstone.pod.utils;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.Product;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.PermissionException;
import com.capstone.pod.filter.ProductSpecificationBuilder;
import com.capstone.pod.jwt.JwtConfig;
import com.capstone.pod.repositories.CredentialRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class Utils {
    public static String buildJWT(Authentication authenticate, Credential credentialAuthenticated, SecretKey secretKey, JwtConfig jwtConfig) {
        String token = Jwts.builder().setSubject(authenticate.getName())
                .claim("authorities", authenticate.getAuthorities())
                .claim("email",credentialAuthenticated.getEmail())
                .claim("credentialId",credentialAuthenticated.getId())
                .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays()))).signWith(secretKey).compact();
        return token;
    };


    public static Specification buildProductSpecifications(String search) {
        ProductSpecificationBuilder builder = new ProductSpecificationBuilder();

        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");

        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Product> spec = builder.build();
        return spec;
    }
}
