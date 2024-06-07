package com.importer.importer.mapstruct;

import com.importer.importer.dto.LogDto;
import com.importer.importer.dto.StudentCreationDto;
import com.importer.importer.dto.StudentCreationDtoByKafka;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "gender", target = "gender")
    StudentCreationDtoByKafka toResponceDto(StudentCreationDto studentCreationDto);


    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "gender", target = "gender")
    LogDto toLogDto(StudentCreationDto studentCreationDto);
}

