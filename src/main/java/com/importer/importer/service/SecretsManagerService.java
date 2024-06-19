package com.importer.importer.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


//@Service
public class SecretsManagerService {
//    private final SecretsManagerClient secretsManagerClient;
//
//    @Autowired
//    public SecretsManagerService(@Value("${aws.accessKeyId}") String accessKeyId,
//                                 @Value("${aws.secretKey}") String secretKey,
//                                 @Value("${aws.region}") String region) {
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
//        this.secretsManagerClient = SecretsManagerClient.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .build();
//    }
//    public String getSecret(String secretName) {
//        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//                .secretId(secretName)
//                .build();
//
//        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
//        System.out.println(getSecretValueResponse + "-----------------------------------------------------------------------");
//        return getSecretValueResponse.secretString();
//    }

}
