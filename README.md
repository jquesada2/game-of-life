## About
This is a simulation of Conway's game of life, developed as a Jetpack Compose Desktop application.

Features include a wraparound resizable grid, variable simulation speed, and interactive cell editing.

## Build Instructions
To build this project, run the following from the project root directory

```
./gradlew run

# or if on windows and not using powershell:
gradlew.bat run
```

Alternatively, a standalone executable jar can be created in 'build/compose/jars' by running
```
./gradlew packageUberJarForCurrentOS
```
The resulting jar file will run as long as the default installed java version is java 8 or later
