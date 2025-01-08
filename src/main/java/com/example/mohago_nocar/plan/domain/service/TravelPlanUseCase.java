package com.example.mohago_nocar.plan.domain.service;

import com.example.mohago_nocar.plan.presentation.request.PlanTravelCourseRequestDto;
import com.example.mohago_nocar.plan.presentation.response.TransitRouteResponseDto;

import java.util.List;

public interface TravelPlanUseCase {

    List<TransitRouteResponseDto> planCourse(PlanTravelCourseRequestDto dto);

}
