## List of changes + working features:

* [07.12.2019] added code for shutting down application by passing argument via commandline (e.g. --shutdown), updated App.class accordingly
* [07.12.2019] now also logging active threads and threads in queue when code is executed and when something is returned to the user
* [07.12.2019] updated Global Messages and added new ones
* [07.12.2019] added comments to threadpoolconfig file
* [07.12.2019] added Disclaimer to the footer of the Webinterface homepage
* [07.12.2019] removed "Snippets" menu from the ffapl-sidebar (contained no useful content)
* [07.12.2019] moved the list entry for "Programmbeispiele" to the menu "Beispiele" from "Spezifikationen" in the `ffapl-sidenav`, since this makes more sense
* [07.12.2019] created new file called CHANGELIST.md (this file) which contains all the changes and working features of the project.

---

* [04.12.2019] added function to copy example code to clip board when viewing "Programmbeispiele" in the Webinterface (updated `ffapl-sidenav-init.js`)
* [04.12.2019] updated unique id generation in javascript; now generates random 20 digit hexadecimal string at the end instead of random 21 alphanumerical symbols
* [04.12.2019] updated Impressum
* [04.12.2019] updated comments and removed redundant ones
* [04.12.2019] added @PreDestroy methods to App class

---

* [27-11-2019] Webinterface now works with Firefox, Chrome, Chromium based Browsers and Edge (Internet Explorer NOT supported anymore!)
* [27-11-2019] added @PreDestroy methods to properly shutdown the application (even with running threads and sunset processes)
* [27-11-2019] moved configutation of ThreadPool to external `threadpoolconfig.properties` file
* [27-11-2019] updated `sunset_shutdown.bat` file to shutdown the SpringBoot Application and all of its subprocesses (currently not a "graceful" shutdown)
* [27-11-2019] added Error400 and Error405 to CustomErrorController

---

* [20-11-2019] saving code in browser now works with newer versions of Edge/IE
* [20-11-2019] added option to stop SpringBoot Application by storing PID of Application Context in local file (`./bin/shutdown.pid`) and then executing a *bat-file which kills the specific process (different commands for Windows and Linux!)
* [20-11-2019] added `sunset_shutdown.bat` file
* [20-11-2019] removed unused js-file
* [20-11-2019] minor changes to pom.xml

---

* [14-11-2019] improved SunsetExecutor class
* [14-11-2019] added logger to main App class

---

* [11-11-2019] added log4j framework and implemented logging (to console and file)
* [11-11-2019] added `log4j.properties` configuration file in `resources`
* [11-11-2019] changed most system.out.println(...) statements with logger statements
* [11-11-2019] updated ID generation in javascript (new structure: UNIX-timestamp in seconds + "-" + string with 22 "random" "characters)
* [11-11-2019] minor improvements to unit tests

---

* [10-11-2019] added code to test abort policy from thread pool when threadpool and queue are full (for Controller and ThreadPool tests!)
* [10-11-2019] added tests for manually canceling execution via http-request
* [10-11-2019] small improvements to other parts of the unit-tests
* [10-11-2019] sunsetexecutor processes now also have an automated timeout, improved code for this class
* [10-11-2019] added new class containing public messages (`SunsetGlobalMessages`)
* [10-11-2019] changed code for thread pool configuration

---

* [09-11-2019] added separate class `SunsetThreadPoolConfiguration?` for setting up the ThreadPool
* [09-11-2019] updated `SunsetThreadPool` class with these changes, removed local variables in this class
* [09-11-2019] added methods for getting infos about the current queue, running threads etc.
* [09-11-2019] updated `SunsetThreadPoolTests` accordingly
* [09-11-2019] updated `SunsetControllerTests`
* [09-11-2019] updated README

---

* [04-11-2019] added test class for testing controller using mockups
* [04-11-2019] it is now possible to test HTTP-requests checking HTTP status codes + values returned by the controller
* [04-11-2019] removed unnecessary comments and improved code structure
* [04-11-2019] removed old files + dead code
* [04-11-2019] improved HTML+javscript code
* [04-11-2019] indentations are now correctly generated when trying to print code in web interface
* [04-11-2019] updated **Impressum** in web interface
 
---

* [23-10-2019] added configuration class for redirecting HTTP traffic (port 8080) to HTTPS
* [23-10-2019] added method annotated with @After in ThreadPoolTest to manually stop the ExecutorService
* [23-10-2019] fixed minor spelling errors and improved formatting

---

* [22-10-2019] added unit test for SunsetThreadPool for testing functionality and exceptions
* [22-10-2019] adjusted code from SunsetThreadPool to be able to test it more efficiently
* [22-10-2019] added `assertJ` to pom.xml
* [22-10-2019] added Exceptions to SunsetThreadPool to prevent illegal (negative) values for timeout and max number of threads
* [22-10-2019] added getters and setters for private variables in SunsetThreadPool
* [22-10-2019] result of SunsetExecutor is now trimmed to remove unnecessary whitespaces
* [22-10-2019] added sunset icon for browser tabs

---

* [21-10-2019] implemented the SunsetThreadPool class to allow concurrent execution of several sunset processes
* [21-10-2019] adjusted code in SunsetController and SunsetExecutor for utilizing the new Thread Pool functionality
* [21-10-2019] added a timeout for automatically interrupting threads after a specified time
* [21-10-2019] users can now manually cancel execution of the code in the Webinterface by pressing the Stop-Button during the execution. Only the corresponding thread and sunset process are interrupted/destroyed.
* [21-10-2019] code is now preserved when user manually cancels the execution of code
* [21-10-2019] added openjfx to pom.xml to be able to use datastructure `Pair`

---

* [15-07-2019] created first version of the SunsetThreadPool class
* [15-07-2019] added basic functionality for testing basic version

---

* [16-04-2019] created custom Error Controller + added error pages (HTML) for `general error`, `error 404` and `error 500`
* [16-04-2019] wrote basic documentation for java-classes and -methods
* [16-04-2019] added unique ID generation in javascript for web interface
   * every time the web interface is loaded a unique ID is created for user
   * ID is sent to controller to identify which user sent which code/request
* [16-04-2019] updated index.html + sunset-max-frontend-functions.js
* [16-04-2019] deleted old/unused *.css-, *.js-files

---

* [04-04-2019] renamed `templates` folder to `static_templates` inside `src/main/resources/static` because there already exists a `templates`-folder which is used by the controller-class
* [04-04-2019] updated code example for RSA to the current version in the `ffapl_api.js`-File
* [04-04-2019] changed name of downloadable files from website (save-button) to automatically look like: `sunsetcode_<DD-MM-YYYY>_<HH-MM-SS>.ffapl`
* [04-04-2019] fixed the issue with the dispatcher servlet (error 500) when executing program via commandline, you can now execute program via the commandline!

---

* [03-04-2019] resolved errors for code examples inside `ffapl_api.js`, changed **single quotes** to **double quotes** inside `print(ln)` statements
* [03-04-2019] included template engine **thymeleaf** (added to `pom.xml`) and updated controller class to show results of the sunset execution in the correct element of the webinterface (`outputTextArea`)
    * controller methods now return `ModelandView` instead of just `String` (plain text)
* [03-04-2019] renamed `/resources/public` to `/resources/templates` to make **thymeleaf** work properly when calling cotroller-methods
* [03-04-2019] modified `index.html` and `sunset-max-stylesheet.css` to better show the `outputTextField` and added code for **thymeleaf**
* [03-04-2019] implemented the **save** function in the `sunset-max-frontend-functions.js`-file for the webinterface (save button) -> you can now download code from the input console as a `*.ffapl`-file and load these files via the **load** button

---

* [02-04-2019] updated the minimal webinterface to the already existing one created by **Peter Pfaffeneder** with some modifications + added all the static web content under `/resources/static`
* [02-04-2019] changed name of java packages for better readability + updated `pom.xml`

---

* [27-03-2019] executing sunset (via commandline) using java + passing code from website
* [27-03-2019] reading sunset output from commandline and sending it to the webinterface (only plain text for now)
* [27-03-2019] parallel execution of several independent user requests already possible (data for each user created locally inside specific controller class, so each user has its own private data stored in memory)
* [27-03-2019] manually cancelling execution of sunset possible (via button on website ONLY while code is executing) **BUT** currently terminates the main `java.exe` process which also terminates all other concurrent sunset executions AND the executable **jar**-file created by Spring-Boot if it is running via the command line

---

* [26-03-2019] created basic controller class for implementing HTTP-Methods (GET, POST etc.)
* [26-03-2019] sending data (code for **sunset**) from webinterface (browser) to java-program (webserver)
* [26-03-2019] communication with server only via port 443 (SSL enabled!) using privately signed certificate for testing (EXPERIMENTAL)

---

* [25-03-2019] created maven-project in eclipse + add dependencies: spring, spring-boot 
* [25-03-2019] created basic structure for program (`src/main/java`, `src/main/resources`, `src/test/java`)
* [25-03-2019] created basic webinterface + javascript

---

## List of features to potentially implement/create in the future:
* add feature to notify users when they have to wait in queue for the calculation or when their calculation starts after being placed in the queue
* add more unit tests
* improve code structure
* include syntax highlighting when printing code via the web interface

---

Last edited @ [07.12.2019]
