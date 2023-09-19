run:
	cd Truco_Java && ant run

jar:
	make clean
	cd Truco_Java && ant jar
	cp -r Truco_Java/src Truco_Java/dist/
	rm Truco_Java/dist/src/**/*.java

clean:
	rm -rf ./Truco_Java/dist/*
