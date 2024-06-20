package com.importer.importer.jwtconfig;

import com.importer.importer.service.SecretsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String secret;

//  @Value("${intern_jwt_secret}")
//  private String jwtSecret;
  
  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  private JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Autowired
  private SecretsManagerService secretsManagerService;

  private static final String[] AUTH_WHITE_LIST = {
          "/v3/api-docs/**",
          "/swagger-ui/**"
  };
  @Bean
  public JwtDecoder jwtDecoder() {
//    System.out.println(jwtSecret + "---------------------------------------------------------");
    String svalue = secretsManagerService.getSecret("intern_manjiri_jwt_secret");
    byte[] decodedKey = svalue.getBytes();
    SecretKey secretKey = new SecretKeySpec(decodedKey, "HMacSHA512");
//    SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA512")
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
     return httpSecurity.authorizeHttpRequests(
         authorize ->
          authorize.requestMatchers(AUTH_WHITE_LIST).permitAll()
          .anyRequest()
          .authenticated()
         )
         .oauth2ResourceServer(configure -> configure.jwt(Customizer.withDefaults())
         .authenticationEntryPoint(jwtAuthenticationEntryPoint))
             .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler))
             .build();
}

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    final JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
    gac.setAuthoritiesClaimName("Role");
    gac.setAuthorityPrefix("ROLE_");
    final JwtAuthenticationConverter jac = new JwtAuthenticationConverter();
    jac.setJwtGrantedAuthoritiesConverter(gac);
    return jac;
  }

}
