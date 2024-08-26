package com.example.running.Service;

import com.example.running.Entity.Coordinate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DistanceServiceTest {

    @Autowired
    private DistanceService distanceService;

    @Test
    public void distanceTest() {
        double latitude1 = 37.504198;
        double longitude1 = 127.047967;

        double latitude2 = 37.501025;
        double longitude2 = 127.037701;


        Coordinate coordinate1 = Coordinate.builder().latitude(latitude1).longitude(longitude1).build();
        Coordinate coordinate2 = Coordinate.builder().latitude(latitude2).longitude(longitude2).build();

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(distanceService.getDistanceOfCircle(coordinate1, coordinate2));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
}