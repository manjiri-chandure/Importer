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
            String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiT0ZGSUNFX0FETUlOIiwiVXNlcklkIjoxLCJzdWIiOiJnYW5lc2hwYXRlbCIsImlhdCI6MTcxMzE3MDgyOCwiZXhwIjoxNzEzMTc4MDI4fQ.yLW7sQLBtQItEPHvbADbY3wRiqp_h7jMu23GJ9QvlNdgpkzgaqG1WMgYgL-COZROMFvXaxbHsU2ganFRKOE0Rw";
            headers.add("Authorization", "Bearer "+ jwtToken );
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<StudentCreationDto> entity = new HttpEntity<StudentCreationDto>(studentCreationDto, headers);


            try {
                ResponseEntity<String> responseEntity = new RestTemplate().exchange("http://localhost:8086/students", HttpMethod.POST, entity, String.class);

                // Append status code and response body to the StringBuilder
                responseBuilder.append("Status Code: ").append(responseEntity.getStatusCode()).append(System.lineSeparator());
                String status = "";
                Integer statusCode = 0;
                String responceMessage = "";
                status = responseEntity.getStatusCode().toString();
                responseBuilder.append("Response Body: ").append(responseEntity.getBody()).append(System.lineSeparator());
                statusCode = Integer.parseInt(status.substring(0, status.indexOf(" ")));
                responceMessage = status.substring(status.indexOf(" ")+1);
                this.logRepository.addLog(studentCreationDto, statusCode, responceMessage);


            } catch (HttpClientErrorException.BadRequest ex) {
                // Handle 400 Bad Request error
                responseBuilder.append("Error: ").append(ex.getStatusCode()).append(" - ").append(ex.getResponseBodyAsString()).append(System.lineSeparator());
            }
            catch (HttpClientErrorException ex) {
                // Handle other 4xx errors
                responseBuilder.append("Error: ").append(ex.getStatusCode()).append(System.lineSeparator());

            } catch (Exception ex) {
                // Handle other exceptions
                responseBuilder.append("Error: ").append(ex.getMessage()).append(System.lineSeparator());
            }
        }

        return responseBuilder.toString();
    }
}
