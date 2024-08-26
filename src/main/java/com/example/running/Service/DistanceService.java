package com.example.running.Service;

import com.example.running.Entity.Coordinate;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    // 두 점이 주어졌을 때 위도, 경도, 고도를 고려한 거리 구하기
    public Double getDistanceBetweenPoints(Coordinate p1, Coordinate p2) {
        return getDistanceOfCircleWithHeight(p1, p2);
    }

    // 고도를 고려한 구면 좌표계에서의 거리 구하기
    public Double getDistanceOfCircleWithHeight(Coordinate p1, Coordinate p2) {
        return Math.sqrt(Math.pow(getDistanceOfCircle(p1, p2), 2) + Math.pow(p2.getHeight()-p1.getHeight(), 2));
    }

    // 구면 좌표계에서의 대원 거리 구하기 (고도 포함X)
    public Double getDistanceOfCircle(Coordinate p1, Coordinate p2) {
        double theta = p2.getLongitude() - p1.getLongitude();

        double dist = Math.sin(deg2rad(p1.getLatitude()))* Math.sin(deg2rad(p2.getLatitude()))
                + Math.cos(deg2rad(p1.getLatitude()))*Math.cos(deg2rad(p2.getLatitude()))*Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist; //단위 meter
    }

    public Double deg2rad(double deg) {
        return (deg * Math.PI/180.0);
    }

    public Double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    // haversine 함수
    public Double haversineFunction(Double centralAngle) {
        return Math.pow(Math.sin(centralAngle/2),2);
    }

}
