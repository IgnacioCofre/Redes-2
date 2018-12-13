all: Client.class Server.class Server_part.class

Client.class: Client.java
	javac Client.java

Server.class: Server.java
	javac Server.java

Server_part.class: Server_part.java
	javac Server_part.java

Server_part:
	java Server_part

clean :
	rm -f *.class
