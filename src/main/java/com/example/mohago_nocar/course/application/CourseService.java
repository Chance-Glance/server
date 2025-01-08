package com.example.mohago_nocar.course.application;

import com.example.mohago_nocar.course.domain.service.CourseUseCase;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.domain.model.TransitRoute;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService implements CourseUseCase {

    private final TransitUseCase transitUseCase;

    public List<Location> getOptimalCourse(List<Location> upcomingVisitLocations) {
        int locationCount = upcomingVisitLocations.size();
        Map<Location, Map<Location, TransitRoute>> fromToTransitInfoMap = new HashMap<>();

        for (int fromIndex = 0; fromIndex < locationCount; fromIndex++) {
            Map<Location, TransitRoute> toLocationTransitInfoMap = new HashMap<>();

            for (int toIndex = 0; toIndex < locationCount; toIndex++) {

                if (fromIndex == toIndex) {
                    continue;
                }

                Location fromLocation = upcomingVisitLocations.get(fromIndex);
                Location toLocation = upcomingVisitLocations.get(toIndex);

                TransitRoute transitRoute = getTransitRouteBetweenLocations(fromLocation, toLocation);
                toLocationTransitInfoMap.put(toLocation, transitRoute);
            }

            fromToTransitInfoMap.put(upcomingVisitLocations.get(fromIndex), toLocationTransitInfoMap);
        }

        List<Location> route = new ArrayList<>();
        List<Boolean> isSelected = new ArrayList<>();
        List<Location> optimalRoute = new ArrayList<>();
        for (int i = 0; i < locationCount; i++) {
            isSelected.add(false);
            optimalRoute.add(upcomingVisitLocations.get(i));
        }

        routeBacktracking(0, upcomingVisitLocations, fromToTransitInfoMap , optimalRoute, route, isSelected);
        return optimalRoute;
    }

    private TransitRoute getTransitRouteBetweenLocations(Location fromLocation, Location toLocation) {
        return transitUseCase.findTransitRoute(fromLocation, toLocation);
    }

    private void routeBacktracking(int k, List<Location> locations, Map<Location, Map<Location, TransitRoute>> transitMaps,
                                   List<Location> optimal, List<Location> route, List<Boolean> isSelected) {
        int n = locations.size();

        if (k == n)
        {
            int t1 = calcTravelTime(optimal, transitMaps);
            int t2 = calcTravelTime(route, transitMaps);

            if (t1 > t2) {
                for (int i = 0; i < n; i++) {
                    optimal.set(i, route.get(i));
                }
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (isSelected.get(i)) {
                continue;
            }

            isSelected.set(i, true);
            route.add(locations.get(i));
            routeBacktracking(k + 1, locations, transitMaps, optimal, route, isSelected);
            route.removeLast();
            isSelected.set(i, false);
        }
    }

    private int calcTravelTime(List<Location> route, Map<Location, Map<Location, TransitRoute>> transitMaps) {
        int n = route.size();

        int travelTime = 0;
        for (int i = 0; i < n - 1; i++) {
            travelTime += transitMaps.get(route.get(i)).get(route.get(i + 1)).getTotalTime();
        }
        return travelTime;
    }

}
