compile :
	mkdir -p bin
	javac -d bin database/*.java model/*.java mapper/*.java main/*.java

run : compile
	java -cp bin main.Main

clean :
	rm -rf bin
