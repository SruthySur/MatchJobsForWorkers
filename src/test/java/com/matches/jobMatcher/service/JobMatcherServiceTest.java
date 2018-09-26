package com.matches.jobMatcher.service;

import com.matches.jobMatcher.Exception.JobMatcherException;
import com.matches.jobMatcher.domain.Job;
import com.matches.jobMatcher.domain.JobSearchAddress;
import com.matches.jobMatcher.domain.Location;
import com.matches.jobMatcher.domain.Worker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class JobMatcherServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    JobMatcherService jobMatcherService = new JobMatcherService();
    @Mock
    RestTemplate restTemplate = new RestTemplate();
    @Mock
    Job job = new Job();
    @Mock
    Worker worker = new Worker();
    @Mock
    Location location = new Location();
    @Mock
    JobSearchAddress jobSearchAddress = new JobSearchAddress();

    private String workerApiUrl = "http://test.swipejobs.com/api/workers";
    private String jobApiUrl = "http://test.swipejobs.com/api/jobs";

    @Before
    public void setUp() throws Exception {

        ReflectionTestUtils.setField(jobMatcherService, "maxJobMatchesAllowed", 3);
        ReflectionTestUtils.setField(jobMatcherService, "workerApiUrl", workerApiUrl);
        ReflectionTestUtils.setField(jobMatcherService, "jobApiUrl", jobApiUrl);
        ReflectionTestUtils.setField(jobMatcherService, "restTemplate", restTemplate);

    }

    @Test
    public void matchingJobsTestsWhenUserIDNotPassed() throws Exception {
        expectedException.expect(JobMatcherException.class);
        jobMatcherService.findMatchingJobs(null);
    }

    @Test
    public void matchingJobsTestsWhenUserIDInvalid() throws Exception {
        expectedException.expect(JobMatcherException.class);
        jobMatcherService.findMatchingJobs(null);
    }

    @Test
    public void matchingJobsMaximum() throws Exception {

        List<Job> matchingJobs = jobMatcherService.findMatchingJobs("8");
        Assert.assertEquals(3, matchingJobs.size());
        ReflectionTestUtils.setField(jobMatcherService, "maxJobMatchesAllowed", 20);
        matchingJobs = jobMatcherService.findMatchingJobs("8");
        Assert.assertEquals(7, matchingJobs.size());
    }

    @Test
    public void certificatesMatch() throws Exception {
        job.setJobId("1");
        job.setRequiredCertificates(Arrays.asList("The Behind the Scenes Wonder"));
        worker.setGuid("worker1");
        worker.setCertificates(Arrays.asList("The Behind the Scenes Wonder"));
        Assert.assertTrue(jobMatcherService.doCertificatesMatch(Arrays.asList("The Behind the Scenes Wonder"), job));
    }

    @Test
    public void locationInMaxDistance() throws Exception {
        job.setJobId("1");

        location.setLongitude("14.293204");
        location.setLatitude("50.266116");
        job.setLocation(location);

        worker.setGuid("worker1");

        jobSearchAddress.setMaxJobDistance("50");
        jobSearchAddress.setLongitude("14.592614");
        jobSearchAddress.setLatitude("50.141097");
        worker.setJobSearchAddress(jobSearchAddress);
        Assert.assertTrue("Job location in desired maximum distance should return true", jobMatcherService.isInMaxJobDistance(location, jobSearchAddress));

        jobSearchAddress.setMaxJobDistance("20");
        worker.setJobSearchAddress(jobSearchAddress);
        Assert.assertFalse("Job location not in desired maximum distance should return false", jobMatcherService.isInMaxJobDistance(location, jobSearchAddress));
    }

    @Test
    public void skillsMatchJobTitle() throws Exception {
        job.setJobId("job2");
        job.setJobTitle("The Resinator");
        worker.setGuid("worker2");
        worker.setSkills(Arrays.asList("Creator of opportunities", "The Resinator"));
        Assert.assertTrue("Job Title matches skills : should return true!", jobMatcherService.doesJobTitleMatchSkills(worker.getSkills(), job));

        job.setJobTitle("Arts and Crafts Designer");
        Assert.assertFalse("Job Title does not match skills : should return false!", jobMatcherService.doesJobTitleMatchSkills(worker.getSkills(), job));

    }
}