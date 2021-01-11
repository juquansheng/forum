package com.uuuuuuuuuuuuuuu.search.repository;


public class GeoEntity {
    private double lat;
    private double lon;

    @Override
    public String toString() {
        return "GeoEntity{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public GeoEntity(double lat, double lon) {

        this.lat = lat;
        this.lon = lon;
    }

}
