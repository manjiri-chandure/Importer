package com.importer.importer.kafka.producer;

import com.importer.importer.dto.StudentCreationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, StudentCreationDto> kafkaTemplate;

    public void sendMessageToTopic(StudentCreationDto studentInfo) {
         CompletableFuture<SendResult<String, StudentCreationDto>> future =  kafkaTemplate.send("students", studentInfo);
         future.whenComplete((result, ex)->{
             if(ex == null){
                 System.out.println("sent message = [" + studentInfo + "] with offset=["+ result.getRecordMetadata().offset() + "]");
             }else{
                 System.out.println("Unable to send message = ["+ studentInfo + "]due to : "+ ex.getMessage());
             }
         });
    }

}