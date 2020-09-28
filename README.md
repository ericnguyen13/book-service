# Book Api Service
The service implemented with spring boot.

Notes:  Because of the time constraint I just implemented the bare minimum functionalities to demonstrate
the knowledge of the spring framework, spring jpa hibernates etc...

### Prerequisites:

* JDK 1.8 or greater

* Gradle 6.0 or greater.

##### please reference the HELP.md for more specific helps.

### Guides
The following guides illustrate how to build, run tests and run the service.

##### build the jar and the jar will be generated in the build/lib folder
    ./gradlew clean build

##### run all tests.
    ./gradlew test

##### run with bootRun.
    ./gradlew bootRun
    
##### run the service from the terminal.
    java -jar build/lib/book-service-0.0.1-SNAPSHOT.jar
    
##### run tests, compile, packaging, and run the service at one command and navigate to the project root then run.
    devOps/run.sh

