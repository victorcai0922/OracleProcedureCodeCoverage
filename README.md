OracleProcedureCodeCoverage
===========================

oracle procedure code coverage tool

Procedure code coverage tools is taken use of oracle DBMS_PROFILER package,which could collect the performance metrics

of every executed code, such as the number of executed times, the execute time of every single line.More referrence could

click this link http://docs.oracle.com/cd/B19306_01/appdev.102/b14258/d_profil.htm.

In this tool, you could collect the code coverage of oracle procedure and get the coverage rate html format report.

User Guide
===========================
Step 1.
Download this project and unzip in your local folder.

Step 2.
Import the project into your eclipse and compile

Step 3.
Make a jar file of this porject 

Step 4.
In your cmd window, you could first check the installation of DBMS_PROFILER of your oracle.

Step 5
If your oracle have not installed the DBMS_PROFILER, you could run the cmd java -jar spcov -create to install dbms_profiler.

Step 6
run the code dbms_profiler.startprofile("start") 
then run your own proceduer 
run the code dbms_profiler.stop() end the profiling.

Step 7
run the command java -jar spcov -report to collect the coverage report.




