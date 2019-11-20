:: Commands for killing the specific process used by the SpringBoot Application Context and deleting the value afterwards for security reasons

:::: Windows Commands
set /p pid=<.\bin\shutdown.pid
TASKKILL /F /PID %pid%
break>.\bin\shutdown.pid

:::: Linux Commands
::kill $(cat ./bin/shutdown.pid)
::echo "" > ./bin/shutdown.pid