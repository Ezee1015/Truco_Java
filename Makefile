run:
	@cd Truco_Java && ant run

jar:
	# Jar
	@make clean
	@cd Truco_Java && ant jar
	# MultiOS
	@mkdir Truco_Java/dist/Truco_Java_MultiOS
	@cp -r Truco_Java/src Truco_Java/dist/Truco_Java_MultiOS
	@rm Truco_Java/dist/Truco_Java_MultiOS/src/**/*.java
	@cp Truco_Java/dist/Truco_Java.jar Truco_Java/dist/Truco_Java_MultiOS/
	@cd Truco_Java/dist && zip -r Truco_Java_MultiOS.zip Truco_Java_MultiOS
	# Windows
	@mkdir Truco_Java/dist/Truco_Java_Windows
	@cp Windows\ BAT\ Installer/Truco.bat Truco_Java/dist/Truco_Java_Windows/
	@mkdir Truco_Java/dist/Truco_Java_Windows/app
	@cp -r Truco_Java/src Truco_Java/dist/Truco_Java_Windows/app/
	@rm Truco_Java/dist/Truco_Java_Windows/app/src/**/*.java
	@mv Truco_Java/dist/Truco_Java.jar Truco_Java/dist/Truco_Java_Windows/app/
	@cd Truco_Java/dist && zip -r Truco_Java_Windows.zip Truco_Java_Windows

clean:
	@rm -rf ./Truco_Java/dist/*
