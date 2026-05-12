compile :
	mkdir -p bin
	javac -d bin -cp "lib/*" database/*.java model/*.java mapper/*.java main/*.java

run : compile
	java -cp "bin;lib/*" main.Main

clean :
	rm -rf bin
