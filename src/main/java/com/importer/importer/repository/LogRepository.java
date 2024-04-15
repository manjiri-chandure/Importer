package com.importer.importer.repository;

import com.importer.importer.dto.StudentCreationDto;
import org.apache.ibatis.annotations.Param;

public interface LogRepository {
    void addLog(@Param("scd") StudentCreationDto studentCreationDto, @Param("sc") Integer statusCode, @Param("rm") String ResponseMessage);
}


