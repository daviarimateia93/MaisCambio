@echo off

set HOST=localhost
set PORT=5432
set USER=postgres
set PASS=R00tP4ssW0rd
set DATABASE=Gerenciarme
set STORAGE_PATH=C:\Users\Administrator\Dropbox\Gerenciarme\Backup\Data\
set POSTGRESQL_PATH=C:\Program Files\PostgreSQL\9.4\

echo Gerenciarme backup has just started...

set HR=%time:~0,2%
if "%HR:~0,1%" equ " " set HR=0%HR:~1,1%
set DATE_STR=%date:~-4,4%_%date:~-10,2%_%date:~-7,2%_-_%HR%_%time:~3,2%_%time:~6,2%
set BACKUP_FILE=%STORAGE_PATH%%DATE_STR%.backup
set PGPASSWORD=%PASS%

echo Executing bg_dump...

cd "%POSTGRESQL_PATH%"
bin\pg_dump -i -h %HOST% -p %PORT% -U %USER% -F c -b -v -f %BACKUP_FILE% %DATABASE% >nul 2>nul

echo Removing oldest files...

cd "%STORAGE_PATH%"
for /f "skip=10 eol=: delims=" %%F in ('dir /b /o-d *.backup') do @del "%%F"

echo End
