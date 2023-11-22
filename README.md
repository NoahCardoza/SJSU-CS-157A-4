# SJSU-CS-157A
Group 4's Project for SJSU's Into to Database Management Systems

## Setup

### Requirements
- MySQL Server 8.0.35 or higher
- IntelliJ IDEA Ultimate 2023.2.4
- Java 16 (OpenJDK)
- Maven
- Tomcat 10.1.13

### Environment Variables / System Properties
- Database Connection
  - **RDS_USERNAME**: This is the username to use when connecting to the database.
  - **RDS_PASSWORD**: This is the password to use when connecting to the database.
  - **RDS_DB_NAME**: The name of the database to use.
  - **RDS_HOSTNAME**: The hostname of the database server.
  - **RDS_PORT**: The port to use when connecting to the database.
- Map Image Generation:
  - **Note**: Not required. If not provided, the application will use a placeholder image. 
  - **GEOAPIFY_API_KEY**: Create an account at [Geoapify](https://www.geoapify.com/) and get an API key.
- Transactional Email:
  - **BREVO_API_KEY**
    - **Note**: Not required when running on `localhost`.
    - Create an account at [Brevo](https://brevo.com/) and get an API key.
- JWT Secret
  - **Note**: Not required. If not provided, the application will use a default secret (`supersecret`). 
  - **JWT_SECRET**: This should be a random string of characters. A good way to generate one is to use `openssl rand -base64 32`.

### Development
1. Open the project in IntelliJ
2. Open the Maven tab on the right side of the screen
3. Click the refresh button to the right of the "Maven Projects" title
4. Install the "Smart Tomcat" plugin by going to File > Settings > Plugins > Marketplace and searching for "Smart Tomcat"
5. Add a new configuration by clicking the dropdown next to the green play button in the top right corner of the screen
6. Click "Edit Configurations..."
7. Click the plus button in the top left corner of the window
8. Click "Smart Tomcat Server"
9. Select a Tomcat server to use.
   1. If you don't have one, click the "Configure..."
   2. Click the plus button in the top left corner of the window
   3. Select the Tomcat installation directory
      1. If you don't have one, download and extract the latest version of Tomcat from 
         [here](https://tomcat.apache.org/download-10.cgi).
      2. Then, return to step 9.iii.
10. Configure the System Properties by clicking the "..." button next to the "VM options" field. Add the following to
    the VM options. Replace the values with the appropriate values for your environment. Refer to the 
    [Environment Variables / System Properties](#environment-variables--system-properties) section for more information.
    ```
    -DRDS_PASSWORD=
    -DRDS_DB_NAME=
    -DRDS_USERNAME=
    -DRDS_HOSTNAME=
    -DRDS_PORT=
    -DGEOAPIFY_API_KEY=
    -DBREVO_API_KEY=
    -DJWT_SECRET=
    ```
11. Click "OK"
12. Run the new configuration by clicking the green play button in the top right corner of the screen.

### Deployment
1. Remember set the JAVA_HOME environment variable to the path of your Java 16 installation.
2. Build the project by running `mvn clean package` in the project directory.
   1. If you don't have Maven installed, you can download it from [here](https://maven.apache.org/download.cgi).
3. Deploy the `target/ROOT-*.war` file to your Tomcat server as `ROOT.war`.
4. Configure the System Properties by adding the following to the `CATALINA_OPTS` environment variable. Replace the 
   values with the appropriate values for your environment. Refer to the 
   [Environment Variables / System Properties](#environment-variables--system-properties) section for more information.
   ```
   -DRDS_PASSWORD=
   -DRDS_DB_NAME=
   -DRDS_USERNAME=
   -DRDS_HOSTNAME=
   -DRDS_PORT=
   -DGEOAPIFY_API_KEY=
   -DBREVO_API_KEY=
   -DJWT_SECRET=
   ``` 
5. (Re)start the Tomcat server to use the new system properties and Java version.
   1. Remember set the JAVA_HOME environment variable to the path of your Java 16 installation.
