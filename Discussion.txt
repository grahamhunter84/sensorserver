Discussion of Sensor project

To start the project you can run maven install.
And then start the SensorCarServer main class.

There is a swagger documentation of the endpoints.

Technology:
Used Spring Boot as it provides a easy to setup framework for using REST conntrollers.
Used hibernate as per the description the number of sensors could be very large and memory intensive.

Architecture:
Due to the problem description the sensor record a large amount of data potentially.
One measurements per minute per sensor which could be in the millions in a single day.
On the other hand the number of sensors is relativly fixed, so to ensure the space complexity
was just O(N), the measurements were not stored in any structure, only the sensors.
 This also reduced the time complexity of each POST and GET call to O(1).

For the alerts we had to store them in a list with the sensor as we need the return the value of the
 measurements that were too high.

To store the sensors a hashmap could have been used - but to simulate a production environment an
in memory database was used (hibernate H2).

The project was split into three layers: controller, service and repository.
The controller handling the REST mapping, and the services with the business logic.
The metric and alert were split into seperate services as to keep the class small. The logic of each was independent.

The Sensor class is the main entity that contains Alert, Measurement and MeasurementAverage.
 As no processing is really done on these other objects I made them embeddable to simplify.

Testing:
For Testing I added integration test cases for the controller, split by Alert and Metrics.
This tests the project end to end.

Not implemented:
I implemented a mechanism for expiring old measurements, to ensure that the average and max would be correct
even if it becomes the next day and value are invalid, but did not have the time to complete it.
For the sake of this project it would be difficult to test without a mock test without waiting for a day.






