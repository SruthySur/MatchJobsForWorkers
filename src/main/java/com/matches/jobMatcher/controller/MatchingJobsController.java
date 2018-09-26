package com.matches.jobMatcher.controller;

import com.matches.jobMatcher.Exception.JobMatcherException;
import com.matches.jobMatcher.service.JobMatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/matches")
public class MatchingJobsController {

    private static final Logger log = LoggerFactory.getLogger(MatchingJobsController.class);

    @Resource
    private JobMatcherService jobMatcherService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public @ResponseBody
    List getMatchingJobs(@PathVariable String userId) {
        try {
            return jobMatcherService.findMatchingJobs(userId);
        } catch (JobMatcherException e) {
            log.warn("Exception : " + e.getMessage());
            return e.getErrors();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody
    String getDefault() {
        return "No userId provided";
    }
}
