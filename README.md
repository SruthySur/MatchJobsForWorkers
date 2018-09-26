# MatchJobsForWorkers
This api takes the userId of a worker and returns no more than three appropriate jobs for that worker. The response from the two given apis are consumed by this REST API.

- http://test.swipejobs.com/api/workers
- http://test.swipejobs.com/api/jobs

# To run application

The application can be run in 2 ways :

 - java -jar jobMatcher-0.0.1-SNAPSHOT.jar

 - Import the code to IDE (Intellij, Eclipse) as maven project, run the JobMatcherApplication

# How to build software
jar file is attached which can be run in java environment. To build, run : mvn clean install. 

# Test the service
Once the service is up, the visit the URL, which returns the JSON for the matching jobs.
- sample: http://localhost:8080/api/matches/8, where 8 is the worker's UserId.
