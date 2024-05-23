package com.importer.importer.dto;

import lombok.Data;

@Data

public class LogDto {
  private Integer lid;
  private String fullName;
  private String gender;
  private Integer age;
  private Integer statusCode;
  private String ResponseMessage;
  private String timeStamp;
}
