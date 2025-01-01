package com.example.mohago_nocar.transit.infrastructure.error.code;

import com.example.mohago_nocar.global.common.exception.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TransitRouteErrorCode implements Status {

    TRANSIT_ROUTE_BATCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BATCH500", "배치 작업 중 문제가 발생했습니다."),
    ITEM_READER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BATCH_READER_500", "데이터 읽기 중 오류가 발생했습니다."),
    ITEM_PROCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BATCH_PROCESSOR_500", "데이터 처리 중 오류가 발생했습니다."),
    ITEM_WRITE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BATCH_WRITER_500", "데이터 쓰기 중 오류가 발생했습니다."),

    TRANSIT_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRANSIT_ROUTE404", "엔티티를 찾을 수 없습니다."),
    ROUTE_SEGMENT_NOT_FOUNT(HttpStatus.NOT_FOUND, "ROUTE_SEGMENT404", "엔티티를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
