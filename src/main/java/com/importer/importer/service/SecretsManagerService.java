package com.importer.importer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.util.Map;


@Service
public class SecretsManagerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${aws.region}")
    private String region;


//    @Autowired
//    public SecretsManagerService(@Value("${aws.accessKeyId}") String accessKeyId,
//                                 @Value("${aws.secretKey}") String secretKey,
//                                 @Value("${aws.region}") String region) {
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
//        this.secretsManagerClient = SecretsManagerClient.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .build();
//        this.objectMapper = new ObjectMapper();
//
//    }
    public String getSecret(String secretName, String secretKey) {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        String secretValue = getSecretValueResponse.secretString();
        System.out.println(secretValue + "--------------------------------------------------------");
        String jwtSecret = null;
        try {
            // Convert JSON string to Map
            Map map = objectMapper.readValue(secretValue, Map.class);
            System.out.println(map + "999999999999999999999999999999999999999999999999999999999999999");

            // Retrieve the value corresponding to the key "intern_jwt_secret"
            jwtSecret = (String) map.get(secretKey);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return jwtSecret;
    }



}
