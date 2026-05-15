compile :
	mkdir -p bin
	javac -d bin -cp "lib/*" database/*.java model/*.java mapper/*.java util/*.java service/*.java driver/*.java

run : compile
	java -cp "bin;lib/*" driver.Main

clean :
	rm -rf bin
