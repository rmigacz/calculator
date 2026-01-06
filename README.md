# Calculator

## Usage

Prerequisites:
- Java JDK 17+ (with GUI support, **not headless**)

Build and run:

```shell
./gradlew clean build
java -jar build/libs/mvc-calculator-1.0-SNAPSHOT.jar
```

Or run `CalculatorApplication.main()` from your IDE.

## Goal
Behavior must strictly follow the defined DFSM:
- See the DFSM diagram: [DFSM diagram](docs/dfsm.puml)

The application follows the [MVC](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) architecture.
