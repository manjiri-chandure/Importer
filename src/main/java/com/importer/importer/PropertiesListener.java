package com.importer.importer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.util.Properties;

@Component("PropertiesListener")
public class PropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {
   private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesListener.class);

   private ObjectMapper mapper = new ObjectMapper();

   private static final String AWS_SECRET_NAME = "intern_manjiri_jwt_secret";
   //private static final String AWS_REGION = "aaaa";

   private static final String password = "db_password";
   private static final String JWT_SECRET = "intern_jwt_secret";

    private SecretsManagerClient secretsManagerClient;

//    @Value("${aws.accessKeyId}")
//    private String accessKeyId;
//
//   // @Value("${aws.secretKey}")
//    private static final String secretKey = "";

   // @Value("${aws.region}")
    private static final String region = "eu-north-1";
   // private final ObjectMapper objectMapper;



    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
       String secretJson = getSecret();

       String database = getString(secretJson, password);
       String jwtSecret = getString(secretJson, JWT_SECRET);
//
//        System.out.println(database+ "-----------------------------------------------------");
//        System.out.println(jwtSecret+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
       ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
       Properties props = new Properties();

       props.put("spring.datasource.password", database);
       props.put("jwt.secret",jwtSecret);
    //   props.put(JWT_SECRET, jwtSecret);

       environment.getPropertySources().addFirst(new PropertiesPropertySource("spring.datasource", props));
//        System.out.println(environment.getPropertySources()+ "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

    }

    private String getSecret(){

      //  AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
      String secret = null;
        try {
                   GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(AWS_SECRET_NAME)
                .build();

        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
       // getSecretValueResult = secretsManagerClient.getSecretValue(getSecretValueRequest);
            if(getSecretValueResponse != null && getSecretValueResponse.secretString() != null){
                secret = getSecretValueResponse.secretString();
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return secret;

    }

    private String getString(String json, String path){
               try {
                   JsonNode root = mapper.readTree(json);
                   return root.path(path).asText();
               }catch (IOException e){
                   LOGGER.error(e.getMessage(),e);
                   return null;
               }
    }
}
