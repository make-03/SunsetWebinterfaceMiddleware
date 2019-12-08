## List of changes + working features:

* [2019-12-07] added code for shutting down application by passing argument via commandline (e.g. --shutdown), updated App.class accordingly
* [2019-12-07] now also logging active threads and threads in queue when code is executed and when something is returned to the user
* [2019-12-07] updated Global Messages and added new ones
* [2019-12-07] added comments to threadpoolconfig file
* [2019-12-07] added Disclaimer to the footer of the Webinterface homepage
* [2019-12-07] removed "Snippets" menu from the ffapl-sidebar (contained no useful content)
* [2019-12-07] moved the list entry for "Programmbeispiele" to the menu "Beispiele" from "Spezifikationen" in the `ffapl-sidenav`, since this makes more sense
* [2019-12-07] created new file called CHANGELIST.md (this file) which contains all the changes and working features of the project.

---

* [2019-12-04] added function to copy example code to clip board when viewing "Programmbeispiele" in the Webinterface (updated `ffapl-sidenav-init.js`)
* [2019-12-04] updated unique id generation in javascript; now generates random 20 digit hexadecimal string at the end instead of random 21 alphanumerical symbols
* [2019-12-04] updated Impressum
* [2019-12-04] updated comments and removed redundant ones
* [2019-12-04] added @PreDestroy methods to App class

---

* [2019-11-27] Webinterface now works with Firefox, Chrome, Chromium based Browsers and Edge (Internet Explorer NOT supported anymore!)
* [2019-11-27] added @PreDestroy methods to properly shutdown the application (even with running threads and sunset processes)
* [2019-11-27] moved configutation of ThreadPool to external `threadpoolconfig.properties` file
* [2019-11-27] updated `sunset_shutdown.bat` file to shutdown the SpringBoot Application and all of its subprocesses (currently not a "graceful" shutdown)
* [2019-11-27] added Error400 and Error405 to CustomErrorController

---

* [2019-11-20] saving code in browser now works with newer versions of Edge/IE
* [2019-11-20] added option to stop SpringBoot Application by storing PID of Application Context in local file (`./bin/shutdown.pid`) and then executing a *bat-file which kills the specific process (different commands for Windows and Linux!)
* [2019-11-20] added `sunset_shutdown.bat` file
* [2019-11-20] removed unused js-file
* [2019-11-20] minor changes to pom.xml

---

* [2019-11-14] improved SunsetExecutor class
* [2019-11-14] added logger to main App class

---

* [2019-11-11] added log4j framework and implemented logging (to console and file)
* [2019-11-11] added `log4j.properties` configuration file in `resources`
* [2019-11-11] changed most system.out.println(...) statements with logger statements
* [2019-11-11] updated ID generation in javascript (new structure: UNIX-timestamp in seconds + "-" + string with 22 "random" "characters)
* [2019-11-11] minor improvements to unit tests

---

* [2019-11-10] added code to test abort policy from thread pool when threadpool and queue are full (for Controller and ThreadPool tests!)
* [2019-11-10] added tests for manually canceling execution via http-request
* [2019-11-10] small improvements to other parts of the unit-tests
* [2019-11-10] sunsetexecutor processes now also have an automated timeout, improved code for this class
* [2019-11-10] added new class containing public messages (`SunsetGlobalMessages`)
* [2019-11-10] changed code for thread pool configuration

---

* [2019-11-09] added separate class `SunsetThreadPoolConfiguration?` for setting up the ThreadPool
* [2019-11-09] updated `SunsetThreadPool` class with these changes, removed local variables in this class
* [2019-11-09] added methods for getting infos about the current queue, running threads etc.
* [2019-11-09] updated `SunsetThreadPoolTests` accordingly
* [2019-11-09] updated `SunsetControllerTests`
* [2019-11-09] updated README

---

* [2019-11-04] added test class for testing controller using mockups
* [2019-11-04] it is now possible to test HTTP-requests checking HTTP status codes + values returned by the controller
* [2019-11-04] removed unnecessary comments and improved code structure
* [2019-11-04] removed old files + dead code
* [2019-11-04] improved HTML+javscript code
* [2019-11-04] indentations are now correctly generated when trying to print code in web interface
* [2019-11-04] updated **Impressum** in web interface
 
---

* [2019-10-23] added configuration class for redirecting HTTP traffic (port 8080) to HTTPS
* [2019-10-23] added method annotated with @After in ThreadPoolTest to manually stop the ExecutorService
* [2019-10-23] fixed minor spelling errors and improved formatting

---

* [2019-10-22] added unit test for SunsetThreadPool for testing functionality and exceptions
* [2019-10-22] adjusted code from SunsetThreadPool to be able to test it more efficiently
* [2019-10-22] added `assertJ` to pom.xml
* [2019-10-22] added Exceptions to SunsetThreadPool to prevent illegal (negative) values for timeout and max number of threads
* [2019-10-22] added getters and setters for private variables in SunsetThreadPool
* [2019-10-22] result of SunsetExecutor is now trimmed to remove unnecessary whitespaces
* [2019-10-22] added sunset icon for browser tabs

---

* [2019-10-21] implemented the SunsetThreadPool class to allow concurrent execution of several sunset processes
* [2019-10-21] adjusted code in SunsetController and SunsetExecutor for utilizing the new Thread Pool functionality
* [2019-10-21] added a timeout for automatically interrupting threads after a specified time
* [2019-10-21] users can now manually cancel execution of the code in the Webinterface by pressing the Stop-Button during the execution. Only the corresponding thread and sunset process are interrupted/destroyed.
* [2019-10-21] code is now preserved when user manually cancels the execution of code
* [2019-10-21] added openjfx to pom.xml to be able to use datastructure `Pair`

---

* [2019-07-15] created first version of the SunsetThreadPool class
* [2019-07-15] added basic functionality for testing basic version

---

* [2019-04-16] created custom Error Controller + added error pages (HTML) for `general error`, `error 404` and `error 500`
* [2019-04-16] wrote basic documentation for java-classes and -methods
* [2019-04-16] added unique ID generation in javascript for web interface
   * every time the web interface is loaded a unique ID is created for user
   * ID is sent to controller to identify which user sent which code/request
* [2019-04-16] updated index.html + sunset-max-frontend-functions.js
* [2019-04-16] deleted old/unused *.css-, *.js-files

---

* [2019-04-04] renamed `templates` folder to `static_templates` inside `src/main/resources/static` because there already exists a `templates`-folder which is used by the controller-class
* [2019-04-04] updated code example for RSA to the current version in the `ffapl_api.js`-File
* [2019-04-04] changed name of downloadable files from website (save-button) to automatically look like: `sunsetcode_<DD-MM-YYYY>_<HH-MM-SS>.ffapl`
* [2019-04-04] fixed the issue with the dispatcher servlet (error 500) when executing program via commandline, you can now execute program via the commandline!

---

* [2019-04-03] resolved errors for code examples inside `ffapl_api.js`, changed **single quotes** to **double quotes** inside `print(ln)` statements
* [2019-04-03] included template engine **thymeleaf** (added to `pom.xml`) and updated controller class to show results of the sunset execution in the correct element of the webinterface (`outputTextArea`)
    * controller methods now return `ModelandView` instead of just `String` (plain text)
* [2019-04-03] renamed `/resources/public` to `/resources/templates` to make **thymeleaf** work properly when calling cotroller-methods
* [2019-04-03] modified `index.html` and `sunset-max-stylesheet.css` to better show the `outputTextField` and added code for **thymeleaf**
* [2019-04-03] implemented the **save** function in the `sunset-max-frontend-functions.js`-file for the webinterface (save button) -> you can now download code from the input console as a `*.ffapl`-file and load these files via the **load** button

---

* [2019-04-02] updated the minimal webinterface to the already existing one created by **Peter Pfaffeneder** with some modifications + added all the static web content under `/resources/static`
* [2019-04-02] changed name of java packages for better readability + updated `pom.xml`

---

* [2019-03-27] executing sunset (via commandline) using java + passing code from website
* [2019-03-27] reading sunset output from commandline and sending it to the webinterface (only plain text for now)
* [2019-03-27] parallel execution of several independent user requests already possible (data for each user created locally inside specific controller class, so each user has its own private data stored in memory)
* [2019-03-27] manually cancelling execution of sunset possible (via button on website ONLY while code is executing) **BUT** currently terminates the main `java.exe` process which also terminates all other concurrent sunset executions AND the executable **jar**-file created by Spring-Boot if it is running via the command line

---

* [2019-03-26] created basic controller class for implementing HTTP-Methods (GET, POST etc.)
* [2019-03-26] sending data (code for **sunset**) from webinterface (browser) to java-program (webserver)
* [2019-03-26] communication with server only via port 443 (SSL enabled!) using privately signed certificate for testing (EXPERIMENTAL)

---

* [2019-03-25] created maven-project in eclipse + add dependencies: spring, spring-boot 
* [2019-03-25] created basic structure for program (`src/main/java`, `src/main/resources`, `src/test/java`)
* [2019-03-25] created basic webinterface + javascript

---

## List of features to potentially implement/create in the future:
* add feature to notify users when they have to wait in queue for the calculation or when their calculation starts after being placed in the queue
* add more unit tests
* improve code structure
* include syntax highlighting when printing code via the web interface

---

Last edited @ [2019-12-08]
