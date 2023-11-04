package com.student.studentdashboardapi.model.mapper;

import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.entity.Student;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public abstract class StudentMapper implements EntityMapper<StudentDto, Student> {

  @Mapping(source = "active", target = "isActive")
  public abstract Student toEntity(StudentDto dto, @Context CycleAvoidingMappingContext context);

  @Mapping(source = "active", target = "isActive")
  public abstract StudentDto toDto(Student entity, @Context CycleAvoidingMappingContext context);


}

