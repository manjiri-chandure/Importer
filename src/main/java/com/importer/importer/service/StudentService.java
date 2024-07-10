package com.importer.importer.service;

import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
//
//
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private LogRepository logRepository;

    @Value("${student.service.baseurl}")
    private String baseUrlLocalPath;


    @Transactional
    public String postAllStudents(List<StudentCreationDto> studentCreationDtos) {
        StringBuilder responseBuilder = new StringBuilder();
        HttpHeaders headers = new HttpHeaders();
        // System.out.println(getSecret() + "-----------------------------------------------------");
        Jwt jwt;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt) {
            jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            throw new RuntimeException("Authentication principal is not instance of jwt");
        }
        String jwtToken = jwt.getTokenValue();
        headers.add("Authorization", "Bearer " + jwtToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        for (StudentCreationDto studentCreationDto : studentCreationDtos) {

            HttpEntity<StudentCreationDto> entity = new HttpEntity<StudentCreationDto>(studentCreationDto, headers);

            String status = "";
            String responseBody = "";
            int statusCode = 0;
            String responseMessage = "";
            try {
                ResponseEntity<String> responseEntity = new RestTemplate().exchange("http://" + baseUrlLocalPath + "/students", HttpMethod.POST, entity, String.class);
                responseBuilder.append("Status Code: ").append(responseEntity.getStatusCode()).append(System.lineSeparator());
                responseBuilder.append("Response Body: ").append(responseEntity.getBody()).append(System.lineSeparator());
                status = responseEntity.getStatusCode().toString();
                statusCode = Integer.parseInt(status.substring(0, status.indexOf(" ")));
                responseMessage = status.substring(status.indexOf(" ") + 1);
                responseBody = "Student Created Successfully";

                responseBuilder.append("Response: ").append(statusCode).append(" ").append(responseMessage).append(" - ").append(responseBody).append(System.lineSeparator());
                logRequest(studentCreationDto, statusCode, responseMessage + ": " + responseBody);
            } catch (HttpClientErrorException ex) {
                // Handle other 4xx errors
                status = ex.getStatusCode().toString();
                statusCode = ex.getStatusCode().value();
                responseMessage = status.substring(status.indexOf(" ") + 1);
                if (!ex.getResponseBodyAsString().isEmpty()) {
                    responseBody = ex.getResponseBodyAsString();
                    responseBuilder.append("Response: ").append(statusCode).append(" ").append(responseMessage).append(" - ").append(responseBody).append(System.lineSeparator());
                    logRequest(studentCreationDto, statusCode, responseMessage + ": " + responseBody);
                } else {
                    responseBuilder.append("Response: ").append(statusCode).append(" ").append(responseMessage);
                    logRequest(studentCreationDto, statusCode, responseMessage);
                }

            } catch (Exception ex) {
                responseBuilder.append("Error: ").append(ex.getMessage()).append(System.lineSeparator());
                statusCode = 500;
                responseMessage = "Internal Server Error";
                responseBuilder.append("Response: ").append(statusCode).append(" ").append(responseMessage);
                logRequest(studentCreationDto, statusCode, responseMessage);
            }
        }

        return responseBuilder.toString();
    }

    private void logRequest(StudentCreationDto studentCreationDto, int statusCode, String message) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        logRepository.addLog(studentCreationDto, statusCode, message, timeStamp);
    }

}
//    public String getSecret() {
//
//        String secretName = "intern_manjiri_jwt_secret";
//        Region region = Region.of("eu-north-1");
//
//        // Create a Secrets Manager client
//        SecretsManagerClient client = SecretsManagerClient.builder()
//                .region(region)
//                .build();
//
//        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//                .secretId(secretName)
//                .build();
//
//        GetSecretValueResponse getSecretValueResponse;
//
//        try {
//            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
//        } catch (Exception e) {
//            // For a list of exceptions thrown, see
//            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
//            throw e;
//        }
//
//        return getSecretValueResponse.secretString();
//
//        // Your code goes here.
//    }
//}
