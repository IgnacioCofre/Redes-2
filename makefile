all: Client.class Server.class Doctor.class Paciente.java Server_part.class 

Client.class: Client.java
	javac Client.java

Server.class: Server.java
	javac Server.java

Doctor.class: Doctor.java
	javac Doctor.java

Paciente.class: Paciente.java
	javac Paciente.java

Server_part.class: Server_part.java
	javac -classpath .:json-simple-1.1.1.jar Server_part.java

Server_part:
	java -classpath .:json-simple-1.1.1.jar Server_part

clean :
	rm -f *.class
