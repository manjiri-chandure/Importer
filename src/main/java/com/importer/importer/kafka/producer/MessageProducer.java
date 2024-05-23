package com.importer.importer.kafka.producer;

import com.importer.importer.dto.StudentCreationDtoByKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, StudentCreationDtoByKafka> kafkaTemplate;

    public void sendMessageToTopic(StudentCreationDtoByKafka studentInfo) {
         CompletableFuture<SendResult<String, StudentCreationDtoByKafka>> future =  kafkaTemplate.send("Student", studentInfo);
         future.whenComplete((result, ex)->{
             if(ex == null){
                 System.out.println("sent message = [" + studentInfo + "] with offset=["+ result.getRecordMetadata().offset() + "]");
             }else{
                 System.out.println("Unable to send message = ["+ studentInfo + "]due to : "+ ex.getMessage());
             }
         });
    }

}