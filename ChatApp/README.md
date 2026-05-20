# Conversations - A minimalistic Chatting Application
## Implementation Details 
## 1. Message Class
Implementation: Implements Serializable.

Purpose: Defines the type of messages available.

Constructor: Includes the type, sender, and the actual content represented by Text.

## 2. ClientHandler
  Inheritance: Extends Thread.

  Properties:
*   Defines multiple clients under a list.
*   Manages Socket, InputStream, and OutputStream.
•   Constructor: Requires a Socket and a List of clients.
•   Overridden run() Method:
*   Defines input and output streams.
*   Receives messages.
*   Extracts message details.
*   Prepares messages to be broadcasted by the server.
•   Methods:
*   Defines a sendMessage function that writes the message object to the stream.
*   Creates a Client object.

## 3. ChatServer 
•   Socket Management: Creates a ServerSocket.

•   Connection: Connects to a socket object using the accept() method.

•   Handling: Establishes a connection between clients through the use of the ClientHandler.

•   Functionality: Handles broadcasting.

### What is Synchronized List?
  Definition: A synchronized (thread-safe) list backed by the specified list.
•   Usage Constraints:
*   In order to guarantee serial access, it is critical that all access to the backing list is accomplished through the returned list.
*   Manual Synchronization: It is imperative that the user manually synchronize on the returned list when traversing it via Iterator, Spliterator, or Stream.
### What are Functional Interfaces? 
 They provide target types for lambda expressions and method references . Each functional interface has a single abstract method , called the functional method for that functional interface, to which the lambda expression's parameter and return types are matched or adapted. 
 
Consumer
