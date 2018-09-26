package com.matches.jobMatcher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobSearchAddress {

    private String unit;
    private String maxJobDistance;
    private String longitude;
    private String latitude;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMaxJobDistance() {
        return maxJobDistance;
    }

    public void setMaxJobDistance(String maxJobDistance) {
        this.maxJobDistance = maxJobDistance;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
