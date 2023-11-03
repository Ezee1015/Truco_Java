@echo off

xcopy /i /E "app" "%HOMEDRIVE%%HOMEPATH%\TrucoJava"

SETLOCAL ENABLEDELAYEDEXPANSION
SET LinkName=Truco Argentino
SET Esc_LinkDest=%%HOMEDRIVE%%%%HOMEPATH%%\Desktop\!LinkName!.lnk
SET Esc_LinkTarget=%%HOMEDRIVE%%%%HOMEPATH%%\TrucoJava\Truco_Java.jar
SET Esc_WorkingDir=%%HOMEDRIVE%%%%HOMEPATH%%\TrucoJava
SET Esc_Icon=%%HOMEDRIVE%%%%HOMEPATH%%\TrucoJava\src\truco_java\fondos\icono.ico
SET cSctVBS=CreateShortcut.vbs
SET LOG=".\%~N0_runtime.log"
((
  echo Set oWS = WScript.CreateObject^("WScript.Shell"^) 
  echo sLinkFile = oWS.ExpandEnvironmentStrings^("!Esc_LinkDest!"^)
  echo Set oLink = oWS.CreateShortcut^(sLinkFile^) 
  echo oLink.TargetPath = oWS.ExpandEnvironmentStrings^("!Esc_LinkTarget!"^)
  echo oLink.WorkingDirectory = oWS.ExpandEnvironmentStrings^("!Esc_WorkingDir!"^)
  echo oLink.IconLocation = oWS.ExpandEnvironmentStrings^("!Esc_Icon!"^)
  echo oLink.Save
)1>!cSctVBS!
cscript //nologo .\!cSctVBS!
DEL !cSctVBS! /f /q
)1>>!LOG! 2>>&1

msg * "Ya se ha instalado el Truco Java en la carpeta del usuario. Se creo un acceso directo en el escritorio. Disfrute!"