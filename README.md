# Guardian - Automation Test Framework
Framework to write and run automation test. Based on : 'TestNG', 'Selenium Web Driver'.

## Installation
1. Install Java 11
2. Install maven version 3.6.3
3. Install chrome version >= 84

## How to use
* Download Project
* Go to project folder
* Run test by command line: 
mvn clean test -Dsuite.testng.file=path_of_suite.xml

Eg: mvn clean test -PTest -Dsuite.testng.file=setting/testng/api/send_qc_report_testng.xml
