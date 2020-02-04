# Sunset Webinterface Middleware

This is the repository for the project **Sunset Webinterface Middleware**. This was the working title for the Bachelor Thesis written by **Markus Ropatsch** for the research group [System Security](https://www.syssec.at/en/) of the [Alpen Adria University Klagenfurt](https://www.aau.at/en/) under the supervision of **Assoc.Prof. Dr. DDipl-Ing. Stefan Rass**. This code is the final result of the implementation and is available as open source under the GNU General Public License v3.0.

This project features a modified version of the already existing web interface created by **Peter Pfaffeneder** as part of an internship at the Alpen Adria University Klagenfurt.

The source code for **Sunset** is publicly available and can be found here: [Sunset Github Repository](https://github.com/stefan-rass/sunset-ffapl)

---

### Generating the Spring Boot Application (Middleware Server)

If you want to generate a jar-file for deploying the middleware server yourself, you need the following software:

* Java 8 JDK (or newer) installed
* IDE supporting Enterprise Java and Web Applications with Build-Management-Tool Maven ([Eclipse](https://www.eclipse.org/downloads/packages/release/2019-12/r/eclipse-ide-enterprise-java-developers) was used for this project)
* Spring Boot (Spring Tools 4 or newer)
* Git or some other software like GitHub Desktop for managing Git-Projects and Repositories.

**Important**: Java 8 or newer is needed to be able to generate the sources for this project, since Maven is configured to use the compiler for Java 8 (Compiler compliance level 1.8), set in the `pom.xml` file under `<properties>`. You do not need to set the compiler level manually in your IDE.

For the first step you use Git to clone this public repository to your local computer. 

After that you load this project into the workspace of your Java IDE. In Eclipse you can do this by going to: `File &#8594; Import &#8594; Git &#8594; Projects from Git (with smart import) &#8594; Existing local repository &#8594; choose the Repository (SunsetWebinterfaceMiddleware [master]) &#8594; choose the folder for the project &#8594; Finish`. Now you should have the imported project visible in the `Project Explorer` or `Package Explorer`. If you see parts of the project hierarchy marked red you might have to do update the maven dependencies defined in the `pom.xml`-file. Right click on the top level folder of the project (`SunsetWebinterfaceMiddleware [boot][devtools][SunsetWebinterfaceMiddleware master]`), go to `maven` and click `Update Project`. This should resolve the issues with certain dependencies.

The last step you need to do is generate the Spring Boot file for deploying the Middleware Server. For this step you need to execute a specific maven Build Run Configuration. For Eclipse go to the top menu bar, click on `Run &#8594; Run Configurations ...`. This should open a window of all Run Configurations defined in this workspace. In the left menu, right click on `maven Build` and select `New Configuration`. For the entry `name` you can personally choose a name for this configuration by yourself. Then for the entry `Base directory` click on `Workspace...` and choose the project name (`SunsetWebinterfaceMiddleware [boot][devtools][SunsetWebinterfaceMiddleware master]`). The last step is to define the `Goals` that maven should consider when generating the files. Here you enter the following goals: `clean validate compile test-compile test package` (separated with a blank space), then click on `Apply` to save these settings and then click on `Run` to start the maven Build. If all these goals were successfully executed you should see a `BUILD SUCCESS` output in the console of your IDE and the generated files in the `target`-folder inside the project, including the `sunsetmiddleware-1.0.0.jar` file, which contains the Spring Boot Application. 

The name of the generated application (`sunsetmiddleware-1.0.0.jar` by default) is defined in the `pom.xml`-file under the entries `<artifactId>` and `<version>` before the `properties` and `parent` entry. These values can be changed. You need to run the maven Build again for these changes to take effect!

---

### Starting the Middleware Server

Now that the Spring Boot Application has been generated, the next step is to start the application and run the middleware sever.<br/> 
**In order to run this software you need Java 8 or newer installed!**

The provided `jar`-file for executing the sunset interpreter is called `sunset.jar`.

**IMPORTANT:** For executing the generated Spring Boot jar file via command line: `keystore.jks`, `sunset.jar` and `threadpoolconfig.properties` must be copied to the same folder from where the generated `jar` file (`sunsetmiddleware-1.0.0.jar`) is executed. These files should also be moved to a different folder than the `target` folder of the project, because the contents of this folder are always deleted every time you start the maven Build defined above. 

When starting the Webserver use the following command via the command line or Power Shell when you are in the same folder as the generated Spring Boot `jar` file:<br/><br/> `java -jar sunsetmiddleware-1.0.0.jar`<br/>

This command should start the Spring Boot Application and make the web interface accessible locally. You can access it by typing the following URL in a modern browser:<br/>`https://localhost`<br/>

The certificate used for testing this project is a private certificate generated by the tool `keytool` inside the `keystore.jks`-file. It is needed because the server can only be accessed via `https` and not `http`. The credentials for using this certificate are stored in the `application.properties`-file under the `CONFIGURATION FOR CERTIFICATE AND HTTPS` section. It was generated on January 18, 2019 and has a validity of 3650 days. Keep in mind that you should only use the private certificate for testing the server locally. If you want to provide access to the web interface over the Internet it is highly advised to use a public certificate that is valid and can be verified by a trusted source.

Once the Server is running there will be new files generated; the first one contains the PID of the Spring Boot Application running (name of this process is "Java" under Windows) and is stored in `./bin/shutdown.pid`. The second one is a log file which contains useful information about the server and the events happening. It is stored in `./logs/sunsetLog.log`.

There are also arguments that the user can pass (from another opened Power Shell / Command Line). For this you also need to be in the folder that contains the `jar` file generated by Spring Boot. There are currently 2 arguments that can be passed:<br/>

* `java -jar sunsetmiddleware-1.0.0.jar --shutdown` The **shutdown** argument shuts down an already running Application of Spring Boot.


* `java -jar sunsetmiddleware-1.0.0.jar --restart` The **restart** argument does the same thing as shutdown during the first step and then starts a new Application of Spring Boot. This is useful if there were changes to some external configuration (like properties file for ThreadPool or logging) without having to Run a maven Build again.

If the user enters an invalid argument or too many an exception is thrown and nothing happens.

Keep in mind when executing code using the middleware server there is a specific timeout in seconds until a calculation is cancelled. The default value for this is 3600 seconds. This value is defined in the `threadpoolconfig.properties`-file and can be changed. It only takes a server restart for these changes to take effect.

---

#### Software used during development:

##### Main programming languages: `Java` for Middleware, `Java Script (+ HTML5, CSS3)` for WebInterface
##### IDE used: `Eclipse Java EE IDE for Web Developers Version 2018-09 (4.9.0)`
##### Build-Management-Tool: `Maven`
##### Frameworks used: `Spring`, `Spring-Boot` (Spring Tools 4 - 4.1.0 RELEASE)
##### Other Software used: `Thymeleaf` (template engine), `Code Mirror`, `JQuery`

---

**UPDATE [2019-12-07]:** Changes and working features moved to the **CHANGELIST.md** file.

---

Last edited @ [2020-02-04]
