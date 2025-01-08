package com.example.mohago_nocar.transit.application.service;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntryRepository;
import com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiUriGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ODsayApiRequestEntryService {

    private final ODsayApiUriGenerator uriGenerator;
    private final FestivalRepository festivalRepository;
    private final FestivalNearPlaceRepository festivalNearPlaceRepository;
    private final OdsayApiRequestEntryRepository apiRequestEntryRepository;

    /**
     * 새로운 축제가 생성되면 주변 장소들과의 모든 이동 경로를 저장하기 위한 API 요청을 생성합니다.
     */
    @Transactional
    public void createOdsayApiRequest(Long festivalId) {
        Festival festival = festivalRepository.getFestivalById(festivalId);
        List<Location> travelLocations = getAllTravelLocations(festival);
        List<OdsayApiRequestEntry> requests = generateApiRequests(travelLocations);

        apiRequestEntryRepository.saveAll(requests);
    }

    private List<Location> getAllTravelLocations(Festival festival) {
        List<FestivalNearPlace> nearPlaces = festivalNearPlaceRepository.findByFestivalId(festival.getId());
        return extractLocationsIn(festival, nearPlaces);
    }

    private List<OdsayApiRequestEntry> generateApiRequests(List<Location> travelLocations) {
        List<OdsayApiRequestEntry> requests = new ArrayList<>();

        for (Location departure : travelLocations) {
            addRequestsForAllArrival(requests, departure, travelLocations);
        }

        return requests;
    }

    private void addRequestsForAllArrival(
            List<OdsayApiRequestEntry> requests,
            Location departure
            , List<Location> arrivals
    ) {
        for (Location arrival : arrivals) {
            if (arrival.equals(departure)) continue;
            requests.add(createApiCallRequest(departure, arrival));
        }
    }

    private OdsayApiRequestEntry createApiCallRequest(Location from, Location to) {
        URI requestURI = uriGenerator.buildRequestURI(
                from.getLongitude(),
                from.getLatitude(),
                to.getLongitude(),
                to.getLatitude()
        );

        return OdsayApiRequestEntry.of(requestURI, RoutePoint.parse(from), RoutePoint.parse(to));
    }

    private List<Location> extractLocationsIn(Festival festival, List<FestivalNearPlace> travelPlaces) {
        return Stream.concat(
                Stream.of(festival.getLocation()),
                travelPlaces.stream().map(FestivalNearPlace::getLocation)
        ).toList();
    }

}
