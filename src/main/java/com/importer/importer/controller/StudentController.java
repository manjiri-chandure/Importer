package com.importer.importer.controller;

import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentDto;
import com.importer.importer.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping("/students")
    @PreAuthorize("hasRole('ROLE_OFFICE_ADMIN')")
    public ResponseEntity<String> postAllStudent(@RequestBody List<StudentCreationDto> studentCreationDtos){
        String studentDtos = this.studentService.postAllStudents(studentCreationDtos);
        return ResponseEntity.ok(studentDtos);
    }

}
