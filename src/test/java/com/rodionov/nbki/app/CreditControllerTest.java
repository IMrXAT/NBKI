package com.rodionov.nbki.app;

import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditGetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreditControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final int NUMBER_OF_CREATION_REQUESTS = 100_000;
    private static final int NUMBER_OF_READ_REQUESTS = 1_000_000;
    private static final int NUMBER_OF_THREADS = 100;
    private static final Random random = new Random();

    private static String uuidToRead;

    @Test
    public void loadTest() throws InterruptedException {
        uuidToRead = sendCreationRequest();

        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_READ_REQUESTS; i++) {
            Thread thread = new Thread(() -> {
                try {
                    responseTimes.add(sendReadRequest());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(thread);
            thread.start();

            if (threads.size() >= NUMBER_OF_THREADS) {
                for (Thread t : threads) {
                    t.join();
                }
                threads.clear();
            }
        }
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("finished receiving requests");

        calculateStatistics(responseTimes);
    }


    @Test
    public void TestCreating100kNewCredit() {

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_CREATION_REQUESTS; i++) {
            sendCreationRequest();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total time (ms): " + (endTime - startTime));
    }


    private Long sendReadRequest() {
        long startTime = System.currentTimeMillis();
        restTemplate.getForEntity("/api/v1/credit/{id}", CreditGetDto.class, uuidToRead);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private void calculateStatistics(List<Long> responseTimes) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Long time : responseTimes) {
            stats.addValue(time);
        }

        System.out.println("Total requests: " + responseTimes.size());
        System.out.println("Total time (ms): " + stats.getSum());
        System.out.println("Median time (ms): " + stats.getPercentile(50));
        System.out.println("95th percentile time (ms): " + stats.getPercentile(95));
        System.out.println("99th percentile time (ms): " + stats.getPercentile(99));
    }

    private String sendCreationRequest() {
        return restTemplate.postForEntity("/api/v1/credit/create", new CreditCreateDto(random.nextDouble()), String.class).getBody();
    }
}
