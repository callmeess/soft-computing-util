# soft-computing-util

implementation for genetic algorithms used for training a neural network.

## Requirements

- Java 17 (or a compatible JDK)
- Maven (optional) — the project includes the Maven Wrapper so Maven is not required to be pre-installed

## Build / Install dependencies

From the repository root (Linux / macOS):

```bash
./soft-util/mvnw clean package -DskipTests
```

Windows (PowerShell / CMD):

```powershell
.\soft-util\mvnw.cmd clean package -DskipTests
```

This will download dependencies, compile the project, and place compiled artifacts in `soft-util/target`.

## Run the examples

There are two simple entry points in the project:

- Genetic algorithm console app: `com.example.softcomputing.genetic.SoftUtilApplication`
- Race simulation (Swing): `com.example.softcomputing.usecase.RaceSimulation`

After building, run either example from the repository root:

```bash
# Run the GA console app
java -cp soft-util/target/classes com.example.softcomputing.genetic.SoftUtilApplication

# Run the race simulation GUI
java -cp soft-util/target/classes com.example.softcomputing.usecase.RaceSimulation
```

If the project has external dependencies packaged into a JAR by the build, you can run the assembled JAR instead (see `soft-util/target` for build outputs).

## Run tests

Use the Maven wrapper to run tests:

```bash
./soft-util/mvnw test
```

## Import into an IDE

Import the `soft-util` folder as a Maven project. The POM targets Java 17. If your IDE supports the Maven Wrapper it will use the included wrapper configuration under `.mvn/wrapper`.

## Configuration

Application properties are in `soft-util/src/main/resources/application.properties`.

## Project structure (high-level)

- `soft-util/src/main/java/com/example/softcomputing/genetic` — genetic algorithm core, chromosomes, operators, builders
- `soft-util/src/main/java/com/example/softcomputing/neuralnetwork` — tiny neural network examples
- `soft-util/src/main/java/com/example/softcomputing/usecase` — example use-cases (race simulation)

## License

This project is licensed under the MIT License. See `LICENSE` for details.

---
