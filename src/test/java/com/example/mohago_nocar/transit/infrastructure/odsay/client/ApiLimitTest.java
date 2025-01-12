package com.example.mohago_nocar.transit.infrastructure.odsay.client;

import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiSuccessResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * ODsay API 호출 횟수를 소모하는 테스트이므로 주석 처리합니다.
 */
@ActiveProfiles("test")
@SpringBootTest
public class ApiLimitTest {

/*    @Autowired
    private ODsayApiUriGenerator odsayApiUriGenerator;

    @Autowired
    RestClient.Builder restClientBuilder;


    @DisplayName("ODsay API는 초당 7번까지 호출이 허용된다.")
    @Test
    public void odsayApiLimitTest() throws InterruptedException {
        //given when
        int low = 1; // 최소 요청 횟수
        int high = 100; // 테스트할 최대 요청 횟수
        int maxAllowed = 0;

        while (low <= high) {
            int mid = (low + high) / 2;

            System.out.println("초당 요청 수 테스트: " + mid + "회");
            boolean success = testApiRate(mid);

            if (success) {
                maxAllowed = mid; // 성공 시 최대 요청 수 갱신
                low = mid + 1; // 더 높은 값 테스트
            } else {
                high = mid - 1; // 실패 시 낮은 값 테스트
            }
        }

        //then
        Assertions.assertThat(maxAllowed).isEqualTo(7);
    }

    @DisplayName("호출 가능한 횟수를 넘으면 요청이 실패한다.")
    @Test
    public void exceededPerSecondApiLimit(){
        //given
        int odsayApiLimitPerSecond = 8;

        //when
        boolean success = testApiRate(odsayApiLimitPerSecond);

        //then
        Assertions.assertThat(success).isFalse();
    }


    private boolean testApiRate(int requestsPerSecond) {
        boolean success = true;

        // 초당 요청 수만큼 반복
        for (int i = 0; i < requestsPerSecond; i++) {
            try {
                makeApiCall();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("429 에러 발생: 요청 수 = " + requestsPerSecond);
                success = false;
                break;
            }

            // 요청 간 간격 조정 (1초 동안 분배)
            try {
                Thread.sleep(1000 / requestsPerSecond);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return success;
    }

    @DisplayName("API 호출 시간을 측정한다.")
    @Test
    public void odsayApiCallTime() throws Exception {
        long startTime = System.currentTimeMillis();
        makeApiCall();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("호출 시간: "+ duration);

    }

    private void makeApiCall() throws Exception {
        ODsayApiSuccessResponse response = restClientBuilder.build().get()
                .uri(odsayApiUriGenerator.buildRequestURI(127.0119379, 37.2871202, 127.0142055, 37.2888038))
                .retrieve()
                .body(ODsayApiSuccessResponse.class);

        System.out.println("API 응답: " + response);
    }*/

}
