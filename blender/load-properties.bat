@echo off
FOR /F "tokens=1,2 delims==" %%G IN (paths.properties) DO (set %%G=%%H)