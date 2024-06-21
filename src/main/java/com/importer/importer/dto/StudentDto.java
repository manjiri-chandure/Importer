package com.importer.importer.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentDto {
    private Integer id;
    private String fullName;
    private String gender;
    private Integer age;
    private List<SubjectDto> subjectDtoList;
}
