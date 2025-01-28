package com.example.mohago_nocar.plan.domain.model;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.Place;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TravelCatalog {

    private final List<TravelLocationWithName> travelLocations;

    public static TravelCatalog of(List<TravelLocationWithName> travelLocations) {
        return TravelCatalog.builder()
                .travelLocations(travelLocations)
                .build();
    }

    public static TravelCatalog of(Festival festival, List<Place> places) {
        List<TravelLocationWithName> travelLocations = new ArrayList<>();
        travelLocations.add(TravelLocationWithName.of(festival));
        travelLocations.addAll(places.stream()
                .map(TravelLocationWithName::of)
                .toList());

        return TravelCatalog.of(travelLocations);
    }

    public List<Location> getDistinctLocations() {
        return travelLocations.stream()
                .map(TravelLocationWithName::getLocation)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean checkDuplicateLocation(Location location) {
        long locationCount = travelLocations.stream()
                .filter(place -> place.getLocation().equals(location))
                .count();
        return locationCount > 1;
    }

    public List<TravelLocationWithName> getTravelLocations(Location location) {
        return travelLocations.stream()
                .filter(place -> place.getLocation().equals(location))
                .toList();
    }

    public Map<Location, String> createLocationNameMap() {
        return this.travelLocations.stream()
                .collect(Collectors.toMap(TravelLocationWithName::getLocation, TravelLocationWithName::getName));
    }

    @Builder
    private TravelCatalog(List<TravelLocationWithName> travelLocations) {
        this.travelLocations = travelLocations;
    }

}
