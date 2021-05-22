FROM minhnhut86/maven_propzy_qa_team:base-selenium

RUN mvn clean test -PTest -Dsuite.testng.file=setting/testng/api/send_qc_report_testng.xml
