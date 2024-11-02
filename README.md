
# Social Media App

CS 180 Team Project



## Compiling Instructions
Instructions on how to compile and run our project

## Submission Record
A list of who submitted which parts of the assignment on Brightspace and Vocareum

ex: 
- Student 1 - Submitted Report on Brightspace
- Student 2 - Submitted Vocareum workspace
## Documentation

A detailed description of each class. This should include the functionality included in the class, the testing done to verify it works properly, and its relationship to other classes in the project. 

- Chat class and Interface: The Chat Class and interface store the message history between two or more users. Each Chat contains identifying information and an ArrayList of the Messages in that chat, with each Message including the author and message type. When a new Chat is created, it is automatically given a unique file that specifies its ID, the IDs of the users involved in the chat, and its detailed message history. This file can be read from using another constructor to instantiate a Chat object, allowing Chats to persist across instances of the project.

- Message class and Interface: The Message Class represents a message that can be sent by a user by storing the sender’s ID and message content (the input that they are trying to send). Every message also has a messageType: the field equals 1 if the message holds an image and 0 if the message does not hold an image. The Message Class is integral to other classes within the project, specifically the Chat class (which stores, records, and manages messages sent between two or more users). Message is mostly used just to store information that will be handled by other classes (such as Chat); therefore, it is simply composed of accessors for its respective fields, a mutator to change the content of a message (if the user wishes to make edits to past messages), and an equals method. 

