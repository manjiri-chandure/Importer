package com.importer.importer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.util.Map;


@Service
public class SecretsManagerService {
    private final SecretsManagerClient secretsManagerClient;

    private final ObjectMapper objectMapper;


    @Autowired
    public SecretsManagerService(@Value("${aws.accessKeyId}") String accessKeyId,
                                 @Value("${aws.secretKey}") String secretKey,
                                 @Value("${aws.region}") String region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.objectMapper = new ObjectMapper();

    }
    public String getSecret(String secretName) {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        String secretValue = getSecretValueResponse.secretString();
        String jwtSecret = null;
        try {
            // Convert JSON string to Map
            Map map = objectMapper.readValue(secretValue, Map.class);

            // Retrieve the value corresponding to the key "intern_jwt_secret"
            jwtSecret = (String) map.get("intern_jwt_secret");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return jwtSecret;
    }



}
