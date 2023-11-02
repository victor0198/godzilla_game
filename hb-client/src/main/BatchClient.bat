@echo off

set "ip=127.0.0.1"
set "port=7777"

:loop
    echo sending POKE
    echo | ncat --send-only %ip% %port%
    timeout /t 1 >nul
goto loop

