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
            String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiUk9MRV9TVFVERU5UIiwic3ViIjoiU2FnYXIiLCJpYXQiOjE3MTI0MDQzMjEsImV4cCI6MTcxMjQxMTUyMX0.O8FOfWr2CxXOs28XlKHejwOblkiXUOqn24iYsb373rhYqI-M1hh7hq0xYyUZ96g46tyzgqdZ61CM-gHK_o2r8Q";
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
