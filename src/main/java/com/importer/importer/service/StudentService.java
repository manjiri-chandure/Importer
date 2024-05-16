package com.importer.importer.service;

import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentDto;
import com.importer.importer.kafka.producer.MessageProducer;
import com.importer.importer.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private MessageProducer messageProducer;


    public String postAllStudents(List<StudentCreationDto> studentCreationDtos) {
            System.out.println(studentCreationDtos.get(0));
            try {
               for(StudentCreationDto studentCreationDto : studentCreationDtos){
                   System.out.println(studentCreationDtos.get(0));
                   messageProducer.sendMessageToTopic(studentCreationDto);
               }
               return "message added to kafka server successfully!!";
           }
           catch (Exception ex){
               return "Exception get come!" + ex.getMessage();
           }
    }
}
