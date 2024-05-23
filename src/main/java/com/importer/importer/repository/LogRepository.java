package com.importer.importer.repository;

import com.importer.importer.dto.LogDto;
import com.importer.importer.dto.StudentCreationDto;
import org.apache.ibatis.annotations.Param;

public interface LogRepository {
    void addLog(@Param("ld")LogDto logDto);
}


