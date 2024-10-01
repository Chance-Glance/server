package com.example.mohago_nocar.course.infrastructure.course;

import com.example.mohago_nocar.course.domain.repository.CourseRepository;
import com.example.mohago_nocar.course.infrastructure.course.CourseJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private CourseJpaRepository courseJpaRepository;
}
