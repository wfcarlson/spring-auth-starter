To Run:

Start postgres on localhost:5432

Create database, and db user with full permission

Update src/main/resources/application.properties with db credentials and name

Update src/main/java/com/example/app/security/SecurityConstants.java SECRET and EXPIRATION_TIME

execute: ./gradlew build && java -jar build/libs/app-0.0.1-SNAPSHOT.jar

