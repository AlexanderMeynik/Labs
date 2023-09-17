package com.example.lab3_2;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class WebConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        KeyStore keyStore;
        HttpComponentsClientHttpRequestFactory requestFactory = null;
        try {
            keyStore = KeyStore.getInstance("jks");
            ClassPathResource classPathResource = new
                    ClassPathResource("gateway.jks");
            InputStream inputStream = classPathResource.getInputStream();
            keyStore.load(inputStream, "password".toCharArray());
            SSLConnectionSocketFactory socketFactory = new
                    SSLConnectionSocketFactory(new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .loadKeyMaterial(keyStore, "password".toCharArray()).build(),
                    NoopHostnameVerifier.INSTANCE);
            HttpClient httpClient =
                    HttpClients.custom().setSSLSocketFactory(socketFactory)
                            .setMaxConnTotal(5)
                            .setMaxConnPerRoute(5)
                            .build();
            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(10000);
            requestFactory.setConnectTimeout(10000);
            restTemplate.setRequestFactory(requestFactory);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return restTemplate;
    }
}