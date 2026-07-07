package com.parikshith.cwm.repository;

import com.parikshith.cwm.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}