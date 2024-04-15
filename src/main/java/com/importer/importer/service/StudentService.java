package com.importer.importer.service;

import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentDto;
import com.importer.importer.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private LogRepository logRepository;
    public String postAllStudents(List<StudentCreationDto> studentCreationDtos){
        StringBuilder responseBuilder = new StringBuilder();
        for(StudentCreationDto studentCreationDto : studentCreationDtos){
            HttpHeaders headers = new HttpHeaders();
            String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiT0ZGSUNFX0FETUlOIiwiVXNlcklkIjoxLCJzdWIiOiJnYW5lc2hwYXRlbCIsImlhdCI6MTcxMzE3ODY2OSwiZXhwIjoxNzEzMTg1ODY5fQ.9vbbK4uk8ywBu13LvUHTPCmyksYjH92vfZXoDsl5sZRSpJLlIwAAziHizcQUeTBnKt24xuLg3WK7O5P_DCt6Yw";
            headers.add("Authorization", "Bearer "+ jwtToken );
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<StudentCreationDto> entity = new HttpEntity<StudentCreationDto>(studentCreationDto, headers);

            String status = "";
            String responseBody = "";
            int statusCode = 0;
            String responseMessage = "";
            try {
                ResponseEntity<String> responseEntity = new RestTemplate().exchange("http://localhost:8086/students", HttpMethod.POST, entity, String.class);
                responseBuilder.append("Status Code: ").append(responseEntity.getStatusCode()).append(System.lineSeparator());
                responseBuilder.append("Response Body: ").append(responseEntity.getBody()).append(System.lineSeparator());
                status = responseEntity.getStatusCode().toString();
                statusCode = Integer.parseInt(status.substring(0, status.indexOf(" ")));
                responseMessage = status.substring(status.indexOf(" ")+1);
                responseBody = "Student Created Successfully";

            } catch (HttpClientErrorException.BadRequest ex) {

                status = ex.getStatusCode().toString();
                statusCode = ex.getStatusCode().value();
                responseMessage = status.substring(status.indexOf(" ")+1);
                if(!ex.getResponseBodyAsString().isEmpty())
                    responseBody = ex.getResponseBodyAsString();
            } catch (HttpClientErrorException ex) {
                // Handle other 4xx errors
                status = ex.getStatusCode().toString();
                statusCode = ex.getStatusCode().value();
                responseMessage = status.substring(status.indexOf(" ")+1);
                if(!ex.getResponseBodyAsString().isEmpty())
                    responseBody = ex.getResponseBodyAsString();

            } catch (Exception ex) {
                responseBuilder.append("Error: ").append(ex.getMessage()).append(System.lineSeparator());
                statusCode = 500;
                responseMessage = "Internal Server Error";
            }
            finally {
                if(!responseBody.isEmpty()){
                    responseBuilder.append("Response: ").append(statusCode).append(" ").append(responseMessage).append(" - ").append(responseBody).append(System.lineSeparator());
                    logRequest(studentCreationDto, statusCode, responseMessage + ": "+ responseBody);
                }
                else{
                    responseBuilder.append("Response: ").append(statusCode).append(" ").append(responseMessage);
                    logRequest(studentCreationDto, statusCode, responseMessage);
                }
            }
        }

        return responseBuilder.toString();
    }

    private void logRequest(StudentCreationDto studentCreationDto, int statusCode, String message) {
        logRepository.addLog(studentCreationDto, statusCode, message);
    }
}
