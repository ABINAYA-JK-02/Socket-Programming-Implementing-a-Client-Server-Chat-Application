# Chat Application

This is a simple socket-based client-server chat application in Java.

## Features
- Multi-client support
- Broadcast messages
- Graceful exit
- Shutdown command

## How to Run
1. Compile: `javac ChatServer.java ChatClient.java`
2. Run Server: `java ChatServer`
3. Run Clients: `java ChatClient`

## Commands
- `EXIT`: Disconnect from server
- `BROADCAST`: Send message to all clients
- `SHUTDOWN`: Stop the server
