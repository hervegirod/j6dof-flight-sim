pushd "%~dp0"
call setenv.bat
"%JAVAHOME%\bin\java" -jar -Djava.library.path=lib j6dof-flight-sim.jar 
popd
pause