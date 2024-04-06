package com.importer.importer.controller;

import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentDto;
import com.importer.importer.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping("/import_students")
    public ResponseEntity<String> postAllStudent(@RequestBody List<StudentCreationDto> studentCreationDtos){
        String studentDtos = this.studentService.postAllStudents(studentCreationDtos);
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/hellow")
    public ResponseEntity<String> sayHellow(){
        return ResponseEntity.ok("hellow meow");
    }
}