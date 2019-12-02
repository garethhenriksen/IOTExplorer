# IOTExplorer
Application to process IOT Messages and allows for analysis of the values.

# Requirements
* Java 8
* Maven
* Apache Kafka
* Postgresql

# Installation
Java
This application requires Java 8 or higher to be installed.

# Maven
This project requires maven to be installed in order to build this project.
Maven can be downloaded from https://maven.apache.org/download.cgi
The following guide can be followed in order to get maven running on Windows
https://howtodoinjava.com/maven/how-to-install-maven-on-windows/

# Kafka Server
A kafka Server instance is required to be running before the application can be built and started.
I followed the following steps to get it running.
Download and extract kafka to C: drive from http://apache.saix.net/kafka/2.3.1/kafka_2.12-2.3.1.tgz
Change PATH variables
Add a new PATH variable under windows environment to the newly extracted kafka server; an example would be
'C:\kafka_2.12-2.3.1\bin\windows'
 
Create a new folder in C:\kafka_2.12-2.3.1\ called data
In C:\kafka_2.12-2.3.1\data create 2 new folders called zookeeper and kafka respectively
These folders hold the data for topics etc.
In C:\kafka_2.12-2.3.1\config
Edit zookeeper.properties to change the properties file dataDir property to C:/kafka_2.12-2.3.1/data/zookeeper
 
Edit server.properties to change the logs.dirs property to C:/kafka_2.12-2.3.1/data/kafka
 
Start Zookeeper service first
Open command prompt and go to C:\kafka_2.12-2.3.1\ run the following command
zookeeper-server-start.bat config\zookeeper.properties
 
Keep this process running
Start kafka server
Open a new command prompt and go to C:\kafka_2.12-2.3.1\ run the following
kafka-server-start.bat config\server.properties
 
If you are not using windows please follow the following guides
https://kafka.apache.org/quickstart 

# Postgresql
For this project a Postgresql database is required
V12.1 was used, available at https://www.enterprisedb.com/thank-you-downloading-postgresql?anid=1257093 
When prompted please choose the password ‘password’ for the default postgres user.

This project will use the default database that gets added with the postgresql installation called ‘postgres’
The port for postgresql is 5432, if the port is defaulted to another please change the port number in the application.yml file (contains database properties) in the project before building.
This can be changed if needed in the project folder /src/main/resources/application.yml
   

# Compile and Run Project
Once kafka and postgres are running the application can be built.
By running ‘mvn clean install’ in the project roots folder it will initialize the build process.

Once the build is completed please change the directory to the ‘target’ folder and run the command ‘java -jar IOT-0.0.1-SNAPSHOT.jar’
 
Once running the application will be available at http://localhost:8080/

Basic Authentication is added to all requests, for demonstration purposes please use username: 'IOT' and password: 'password'
