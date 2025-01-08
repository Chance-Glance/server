package com.example.mohago_nocar.course.domain.service;

import com.example.mohago_nocar.global.common.domain.vo.Location;

import java.util.List;

public interface CourseUseCase {
    
    List<Location> getOptimalCourse(List<Location> upcomingVisitLocations);

}
