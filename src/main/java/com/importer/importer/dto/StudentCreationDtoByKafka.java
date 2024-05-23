package com.importer.importer.dto;

import lombok.Data;

@Data
public class StudentCreationDtoByKafka {
    private Integer lid;  // id corresponding to log table entry
    private String fullName;
    private String gender;
    private Integer age;
}
