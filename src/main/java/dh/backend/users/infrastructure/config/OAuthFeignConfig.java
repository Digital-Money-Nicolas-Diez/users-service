package dh.backend.users.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import feign.RequestInterceptor;

@Configuration
public class OAuthFeignConfig {
  Logger log = LoggerFactory.getLogger(OAuthFeignConfig.class);

  @Bean
  public RequestInterceptor oauth2Interceptor(
      OAuth2AuthorizedClientManager manager) {
    return request -> {
      OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest
          .withClientRegistrationId("account-service")
          .principal("users-service")
          .build();

      OAuth2AuthorizedClient client = manager.authorize(authRequest);

      if (client != null) {
        request.header(
            "Authorization",
            "Bearer " + client.getAccessToken().getTokenValue());
      }
    };
  }
}
