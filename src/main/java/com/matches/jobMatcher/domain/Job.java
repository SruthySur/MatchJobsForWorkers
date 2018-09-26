package com.matches.jobMatcher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    private String guid;
    private String company;
    private Boolean driverLicenseRequired;
    private String jobTitle;
    private List<String> requiredCertificates;
    private String billRate;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Boolean getDriverLicenseRequired() {
        return driverLicenseRequired;
    }

    public void setDriverLicenseRequired(Boolean driverLicenseRequired) {
        this.driverLicenseRequired = driverLicenseRequired;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<String> getRequiredCertificates() {
        return requiredCertificates;
    }

    public void setRequiredCertificates(List<String> requiredCertificates) {
        this.requiredCertificates = requiredCertificates;
    }

    public String getBillRate() {
        return billRate;
    }

    public void setBillRate(String billRate) {
        this.billRate = billRate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private Location location;


}
