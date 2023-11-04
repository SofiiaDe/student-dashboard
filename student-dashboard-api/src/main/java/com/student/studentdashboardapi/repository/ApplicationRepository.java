package com.student.studentdashboardapi.repository;

import com.student.studentdashboardapi.model.entity.Application;
import com.student.studentdashboardapi.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByStudent(Student student);
}
