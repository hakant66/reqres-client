# reqres-client

ReqRes User Client
This is a simple Java application that demonstrates how to consume a RESTful API using Java's built-in HTTP client and Google's Gson library. It fetches and displays paginated user data from the ReqRes API (https://reqres.in/), a free API for testing and prototyping.
* This README.md file is created by AI.
* Code is built for learning and demonstration purposes.
Dependencies
- Java 11+
- Gson for JSON parsing
Project Structure (basic)

src/
├── main/
│   ├── java/
│   │   └── com/example/reqresclient/
│   │       ├── UserDTO.java
│   │       └── UserClient.java
│   └── resources/
│       └── config.properties

Features
- Connects to a REST API endpoint to fetch users page-by-page.
- Deserializes JSON responses into Java objects using Gson.
- Supports debug mode to print detailed user info.
- Handles errors gracefully and provides descriptive error messages.
How to Run
Build and Run

Using Maven:
Open your terminal or command prompt and navigate to the reqres-client directory, which contains your pom.xml file. 
mvn clean compile 
mvn package 
cd target java -jar reqres-client-1.0-SNAPSHOT.jar 

Without Using Maven:
To build your Java code without Maven, you'll need to manually handle compilation, dependency management, and JAR creation using command-line tools like javac and jar that come with your Java Development Kit (JDK). 
Windows (Command Prompt/Powershell): 
javac -d target\classes -cp lib\gson-2.10.1.jar src\main\java\com\example\reqresclient\*.java

copy src\main\resources\config.properties target\classes\config.properties

jar -c -v -f reqres-client.jar -m MANIFEST.MF -C target/classes .

java -jar reqres-client.jar



Linux (Bash):
javac -d target/classes -cp lib/gson-2.10.1.jar src/main/java/com/example/reqresclient/*.java

cp src/main/resources/config.properties target/classes/config.properties

jar -cvfm reqres-client.jar MANIFEST.MF -C target/classes .
    • 
java -jar reqres-client.jar

📘 Example Output

Using API Base URL from config: https://reqres.in/api/users
Debugging Enabled: true

--- Fetching All Users Across Pages ---

--- Users on Page 1 ---
Name: George Bluth
  Debug Info: UserDTO{id=1, email='george.bluth@reqres.in', firstName='George', lastName='Bluth'}
...




