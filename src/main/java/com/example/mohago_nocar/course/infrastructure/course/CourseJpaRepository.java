package com.example.mohago_nocar.course.infrastructure.course;

import com.example.mohago_nocar.course.domain.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {
}
