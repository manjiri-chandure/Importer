package com.importer.importer.service;

import com.importer.importer.dto.LogDto;
import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentCreationDtoByKafka;
import com.importer.importer.kafka.producer.MessageProducer;
import com.importer.importer.mapstruct.StudentMapper;
import com.importer.importer.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private StudentMapper studentMapper;

    public String postAllStudents(List<StudentCreationDto> studentCreationDtos) {
            List<StudentCreationDtoByKafka> studentResponceDtos = new ArrayList<>();
            try {
               for(StudentCreationDto studentCreationDto : studentCreationDtos){
                   String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                   LogDto logDto = this.studentMapper.toLogDto(studentCreationDto);
                   logDto.setStatusCode(0);
                   logDto.setResponseMessage("Status Pending...");
                   logDto.setTimeStamp(timeStamp);
                   logRepository.addLog(logDto);
                   StudentCreationDtoByKafka studentCreationDtoByKafka = this.studentMapper.toResponceDto(studentCreationDto);
                   studentCreationDtoByKafka.setLid(logDto.getLid());
                   studentResponceDtos.add(studentCreationDtoByKafka);
               }
               for(StudentCreationDtoByKafka studentCreationDtoByKafka : studentResponceDtos){
                   System.out.println("\n\n"+studentCreationDtoByKafka);
                   messageProducer.sendMessageToTopic(studentCreationDtoByKafka);
               }
               return "message added to kafka server successfully!!";
           }
           catch (Exception ex){
               return "Exception get come!" + ex.getMessage();
           }
    }

    @KafkaListener(topics = "logs", groupId = "student-log", containerFactory = "kafkaListenerContainerFactory")
    public void updateLogs(LogDto logDto){
        System.out.println(logDto + "----------------------------------------------");
        Integer lid = logDto.getLid();
        String rm = logDto.getResponseMessage();
        Integer sc = logDto.getStatusCode();
         this.logRepository.updateLog(lid,rm ,sc);
    }
}
