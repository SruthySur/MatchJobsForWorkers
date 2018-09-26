package com.matches.jobMatcher.service;

import com.matches.jobMatcher.Exception.JobMatcherException;
import com.matches.jobMatcher.domain.Job;
import com.matches.jobMatcher.domain.JobSearchAddress;
import com.matches.jobMatcher.domain.Location;
import com.matches.jobMatcher.domain.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JobMatcherService {

    private static final Logger log = LoggerFactory.getLogger(JobMatcherService.class);
    @Resource
    public RestTemplate restTemplate;
    @Value("${max.job.matches.allowed}")
    private int maxJobMatchesAllowed;
    @Value("${workers.api.url}")
    private String workerApiUrl;
    @Value("${jobs.api.url}")
    private String jobApiUrl;

    public List<Job> findMatchingJobs(String userId) throws JobMatcherException {

        Worker[] workers = restTemplate.getForObject(workerApiUrl, Worker[].class);
        Job[] jobs = restTemplate.getForObject(jobApiUrl, Job[].class);

        Worker currentWorker = Arrays.stream(workers).filter(worker -> worker.getUserId().equals(userId)).findFirst().orElse(null);
        List<Job> matchingJobs;

        if (currentWorker == null) {
            log.info("Worker with " + userId + " does not exist");
            throw new JobMatcherException("No worker with the userId : " + userId, "101");
        } else {
            log.info("Worker with userId :  " + currentWorker.getUserId());
            List<String> skills = currentWorker.getSkills();
            List<String> certificates = currentWorker.getCertificates();
            Stream<Job> jobStream;

            if (!currentWorker.getHasDriversLicense()) {
                jobStream = Arrays.stream(jobs).filter(job -> (!job.getDriverLicenseRequired()));
            } else {
                jobStream = Arrays.stream(jobs);
            }

            matchingJobs = jobStream
                    .filter(job -> doesJobTitleMatchSkills(skills, job))
                    .filter(job -> doCertificatesMatch(certificates, job))
                    .filter(job -> isInMaxJobDistance(job.getLocation(), currentWorker.getJobSearchAddress()))
                    .limit(maxJobMatchesAllowed)
                    .collect(Collectors.toList());
            return matchingJobs;
        }
    }

    protected boolean doCertificatesMatch(List<String> certificates, Job job) {
        return certificates.containsAll(job.getRequiredCertificates());
    }

    protected boolean doesJobTitleMatchSkills(List<String> skills, Job job) {
        return skills.stream().anyMatch(skill -> skill.equals(job.getJobTitle()));
    }

    protected boolean isInMaxJobDistance(Location location, JobSearchAddress jobSearchAddress) {
        double jobLatitude = convertToDouble(location.getLatitude());
        double jobLongitude = convertToDouble(location.getLongitude());
        double addressLatitude = convertToDouble(jobSearchAddress.getLatitude());
        double addressLongitude = convertToDouble(jobSearchAddress.getLongitude());
        double distance = getDistanceInKm(jobLatitude, jobLongitude, addressLatitude, addressLongitude);
        log.info("Distance : " + distance + " -- MaxJobDistance : " + jobSearchAddress.getMaxJobDistance());
        if (distance <= Double.parseDouble(jobSearchAddress.getMaxJobDistance()))
            return true;
        else
            return false;
    }

    private double convertToDouble(String value) {
        return Double.parseDouble(value);
    }

    private double getDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371;
        double LatitudeInRadian = convertToRadian(lat2 - lat1);
        double LongitudeInRadian = convertToRadian(lon2 - lon1);
        double distance = Math.sin(LatitudeInRadian / 2) * Math.sin(LatitudeInRadian / 2) +
                Math.cos(convertToRadian(lat1)) * Math.cos(convertToRadian(lat2)) *
                        Math.sin(LongitudeInRadian / 2) * Math.sin(LongitudeInRadian / 2);
        distance = 2 * Math.atan2(Math.sqrt(distance), Math.sqrt(1 - distance));
        double distanceInKM = earthRadius * distance;
        return distanceInKM;
    }

    private double convertToRadian(double degrees) {
        return degrees * (Math.PI / 180);
    }


}


