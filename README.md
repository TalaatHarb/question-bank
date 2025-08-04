# question-bank
Small desktop application for having questions organized. Can be useful for a talking panel or a podcast

<img src="./img/ScreenShot01.PNG.PNG">

## Features

- Create question bank
- Edit question bank
- Open question bank

## Technical Overview

question-bank is built using JavaFX for a modern, responsive UI and integrates technologies like:

- **Jackson**: For JSON parsing, enabling fast processing and manipulation of JSON data.
 
## How to Use

1. Download the latest release JAR file from [Releases](https://github.com/talaatharb/question-bank/releases).
 
2. Run the application using Java:

```java
 java -jar question-bank-<version>. jar
```


## Requirements

- **Java 21+** for running the project
- **Maven 3.x** (for building the project only)
  
## How to Build

1. Clone the repository:
   ```bash
   git clone https://github.com/talaatharb/question-bank.git
   ```
2. Navigate to the project directory:
   ```bash
   cd question-bank
   ```
3. Build the project using Maven:
   ```bash
   mvn clean compile package
   ```
4. Run the application:
   ```bash
   mvn javafx:run
   ```

## Contributions
Contributions are welcome! Feel free to open issues for bugs, feature requests, or submit pull requests. Make sure to follow the contribution guidelines.
