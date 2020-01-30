The task consists in writing a pair of tasks – a server and a client, implementing “authorisation” using “UDP port knocking” (https://en.wikipedia.org/wiki/Port knocking).

All required functionalities have been implemented as follows: 
•	Any number of clients can try to authorize at once.
•	Server sends the number of port to the address, from which a proper sequence of UDP packets was received via UDP.

Server process is implemented in the class Server(main( )) using auxiliary classes: 
•	MyServerSocket - listens for UDP knocking,
•	ClientKey - represents information about the knocking clients,
•	UDP - contains static configuration for UDP packets,
•	TCPServer and TCPServerThread– used for transferring the file
Client process is implemented in class Client(main( )) also using the class UDP for packets configuration.

Server:
1.	Numbers of ports for listening the knocking are extracted from main( ) arguments
2.	Instance of the class Server is created (with a sequence and set of ports for listening as parameters)
3.	For each port in a set of ports a new Thread is created and started - in each of these Threads an instance of the class MyServerSocket is created (with Server object and number of a port for listening as parameters)
4.	On each instance of MyServerSocket method listen( ) is called, which in an infinite loop receives a knocking datagrams from clients
5.	After receiving any knocking datagram a new Thread is created and started – in a Thread method knock( ) of the class Server is called for checking the sequence of knocks from a client
6.	If the last matching knock of an expected sequence from a checked client is received, method respontTo( ) of the class Server is called
7.	In method respontTo( ) an instance of a class TCPServer is created on a random port, then this port number is sent to the client via UDP DatagramSocket 
8.	Method listen( ) of class TCPServer is called in which a an instance of TCPServerThread is created and started
9.	TCPServerThread accepts a client socket, sends filename, file length and the content of the file
Client:
1.	Server IP address and numbers of ports for knocking are extracted from main( ) arguments
2.	For each port in sequence a knocking DatagramPacket sent via UDP DatagramSocket
3.	After the whole sequence of knocks is sent client attempts to receive a DatagramPacket from the server via UDP DatagramSocket
4.	If the answer (the server`s TCP port number for uploading a file) is received, a TCP Socket is opened for that port and 
5.	Then file name, length and content are received from the server via the TCP Socket
