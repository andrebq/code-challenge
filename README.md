# Info

To build and test the project, call:

```
./gradlew clean test
```

# IntelliJ

To setup the project using IntelliJ IDEA, call:

```
./gradlew idea
```

It uses Lombok, [it is possible to setup IntelliJ to understand lombok annotations](https://projectlombok.org/setup/intellij)

# Running

To run:

```
./gradlew bootRun
```

To run in debug mode

```
./gradlew bootRun --debug-jvm
```

# Project explanation

* Multi-thread access is controlled by spring-boot
* SummaryRepositoryImpl is responsible for controlling access from multiple-threads.
* TransactionServiceImpl is responsible for validating input and controlling the size of the data,
    to ensure O(1) operations (read/write).

The underlying implementation uses a TreeMap (big-O(log n)) but there is a upper-bound limiting the TreeSize,
regardless of how many transactions happens at any given millisecond.

Therefore even with more than 1 transaction per millisecond, **log n** will be a constant,
therefore the final implementation will be big-O(K) where K is constant thus giving big-O(1).
