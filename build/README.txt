To start the application:

1. Update the JAVA_HOME in the skunktool3.bat file

2. Update the resource/skunkwerk_support_tool.properties with
2.a. list of the http interop (URL STERLING_INTEROP_URL)
2.b. the javadoc location (STERLING_API_JAVADOC_URL)
2.c. allow advance api tester (TESTER_MODE)

3. To create additional Rapid Tester UI Form. create a folder in the apirunner_file_store/11_test_harness/harness folder. Use one of the existing folder for reference.


4. If you need to run the application without http connection:
4.a. Put the jars from scmfs.ear into the lib_ear folder
4.b. Put any additional properties files in the properties folder (they get loaded before properties.jar in the lib_ear folder)

