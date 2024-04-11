package com.importer.importer.service;

import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    public String postAllStudents(List<StudentCreationDto> studentCreationDtos){
        String responce = "";
        for(StudentCreationDto studentCreationDto : studentCreationDtos){
            HttpHeaders headers = new HttpHeaders();
            String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiT0ZGSUNFX0FETUlOIiwiVXNlcklkIjoxLCJzdWIiOiJnYW5lc2hwYXRlbCIsImlhdCI6MTcxMjg0NzE4NywiZXhwIjoxNzEyODU0Mzg3fQ.kFCj8gM2Iybt-CgJEYxS1QaKwBxKhOrYYd2wP2H9gQG33A6fL07Wss3woS9AwWUa1BvyYqrjITWzN3J_ZWAFfA";
            headers.add("Authorization", "Bearer "+ jwtToken );
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<StudentCreationDto> entity = new HttpEntity<StudentCreationDto>(studentCreationDto, headers);
            String temp = "";
            temp += new RestTemplate().exchange("http://localhost:8086/students", HttpMethod.POST, entity, String.class).getBody();
            responce += temp;
        }
        return  responce;
    }
}
