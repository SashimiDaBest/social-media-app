
# Social Media App
CS 180 Team Project

## Table of Contents
- [Compilation and Execution Instructions](https://github.com/SashimiDaBest/social-media-app/tree/changingBranch?tab=readme-ov-file#compilation-and-execution-instructions)
- [Submission Record](#Submission Record)
- [Simplified Project Layout](#Simplified Project Layout)
- [Documentation](#Documentation)
- [Testing Guide](#Testing Guide)

## Compilation and Execution Instructions

1. **Starting the Server:**
    - Open your terminal and navigate to the directory containing `SimpleServer.java`.
    - Compile and run the server using the following commands:
      ```bash
      javac SimpleServer.java && java SimpleServer
      ```

2. **Starting the Client:**
    - Open a separate terminal and navigate to the directory containing `ClientHandler.java`.
    - Compile and run the client using the following commands:
      ```bash
      javac ClientHandler.java && java ClientHandler
      ```
      
## Submission Record
A list of who submitted which parts of the assignment on Brightspace and Vocareum
- Connor Pugliese - Submitted Vocareum workspace v1 for Phase 1
- Derek McTume - Submitted Vocareum workspace v2 for Phase 1
- Soleil Pham - Submitted Vocareum workspace v3 for Phase 1

## Simplified Project Layout
```bash
src/ 
│
├── clientPageOperation/           # Client-side operations and functionalities
│   ├── FeedPageClient.java        # Handles operations related to the user's feed page
│   ├── OtherPageClient.java       # Manages interactions with other users' profile pages
│   ├── UserPageClient.java        # Manages operations for the user's own profile page
│   └── WelcomePageClient.java     # Handles the welcome page operations
│
├── exception/                     # Custom exceptions for handling errors
│   ├── InvalidCreateAccountException.java # Thrown when account creation fails due to invalid fields
│   ├── InvalidFileFormatException.java    # Thrown when a file format is invalid or corrupted
│   └── NoChatFoundException.java          # Thrown when a specified chat is not found
│
├── object/                        # Object classes and interfaces
│   ├── Chat.java                  # Chat class for managing chat-related operations
│   ├── ChatInterface.java         # Interface for chat operations
│   ├── Message.java               # Message class for managing individual messages
│   ├── MessageInterface.java      # Interface for message operations
│   ├── User.java                  # User class for managing user-related operations
│   └── UserInterface.java         # Interface for user operations
│
├── serverPageOperation/           # Server-side operations and functionality
│   ├── FeedPageServer.java        # Handles feed page operations on the server
│   ├── OtherPageServer.java       # Handles other user page operations on the server
│   ├── UserPageServer.java        # Handles user profile operations on the server
│   └── WelcomePageServer.java     # Handles welcome page operations on the server
│
├── uiPage/                        # User interface and UI-related classes
│   ├── CreateUserPage.java        # Page for user creation
│   ├── FeedViewPage.java          # Page for displaying the user's feed
│   ├── OtherProfilePage.java      # Page for viewing other users' profiles
│   ├── UserProfilePage.java       # Page for viewing the user's own profile
│   └── WelcomePage.java           # Welcome page for the application
│
├── ClientHandler.java             # Main client handler class
├── MessageTest.java               # Test class for message functionality
├── RunChatTests.java              # Test runner for chat-related tests
├── RunUserTests.java              # Test runner for user-related tests
├── ServerInterface.java           # Interface for server operations
├── SimpleServer.java              # Main server class
├── SimpleServerTest.java          # Test class for the SimpleServer
└── UserTest.java                  # Test class for the User object
```

## Documentation
A comprehensive overview of each class, covering its functionality, the testing conducted to ensure it operates correctly, and its interactions with other classes within the project.

### 1. **Chat Class**
- **Functionality**: Manages chat sessions by storing messages and participant details. Includes fields for chat ID, user IDs, and an array list for message history. Saves to a file, allowing chat history persistence across sessions.
- **Testing**: Verified through the `RunChatTests` class, which includes tests for adding, editing, and deleting messages, and ensures data persistence.
- **Relationships**: Uses `Message` for storing messages within each chat and `InvalidFileFormatException` for handling improperly formatted files. Connects with `User` to manage user-specific chats.

### 2. **ChatInterface**
- **Functionality**: Defines required methods for chat functionality, ensuring a consistent structure in implementing classes.
- **Testing**: Not directly tested but enforces required operations within the `Chat` class.
- **Relationships**: Used by `Chat` to provide a structured approach to chat functionalities.

### 3. **ClientHandler Class**
- **Functionality**: Manages client interactions with the `SimpleServer`, enabling real-time communication and chat functionality.
- **Testing**: Tested as part of server functionality with `SimpleServer`.
- **Relationships**: Works with `SimpleServer` to manage client-server connections and interacts with `Chat` and `User` to facilitate chat operations.

### 4. **InvalidFileFormatException Class**
- **Functionality**: Custom exception for handling file format errors in chat and user data, ensuring data consistency.
- **Testing**: Tested when improperly formatted files are loaded, as in `RunChatTests`.
- **Relationships**: Used by `Chat` and `User` classes to enforce correct file formats.

### 5. **Main Class**
- **Functionality**: The entry point for the application, initializing components and setting up connections between classes.
- **Testing**: Verified through the application’s runtime, ensuring components initialize and interact correctly.
- **Relationships**: Coordinates `SimpleServer`, `ClientHandler`, `User`, and `Chat` classes, creating a unified environment.

### 6. **Message Class**
- **Functionality**: Represents individual messages, including fields for sender ID, content, and type (e.g., text or image).
- **Testing**: The `MessageTest` class validates message creation, content retrieval, and mutation.
- **Relationships**: Integral to `Chat` for storing messages, with `MessageInterface` providing a standard structure.

### 7. **MessageInterface**
- **Functionality**: Defines essential methods for messages, ensuring consistency in message operations.
- **Testing**: Ensures `Message` and related classes implement required methods.
- **Relationships**: Implemented by `Message` to standardize message functionalities.

### 8. **MessageTest Class**
- **Functionality**: Unit tests for `Message`, verifying content retrieval, author ID retrieval, and updates.
- **Testing**: Confirms `Message` works as expected in different scenarios.
- **Relationships**: Directly tests `Message` functionality.

### 9. **NoChatFoundException Class**
- **Functionality**: Custom exception for when a requested chat is not found, supporting error handling.
- **Testing**: Tested when invalid chat IDs are referenced.
- **Relationships**: Used by `User` and `Chat` classes to handle missing chats.

### 10. **RunChatTests Class**
- **Functionality**: Tests `Chat`, verifying file creation, message handling, and data persistence.
- **Testing**: Covers chat functionalities like adding, editing, and deleting messages, and ensures data loading from files.
- **Relationships**: Directly tests `Chat` and validates its interactions.

### 11. **RunUserTests Class**
- **Functionality**: Tests `User` methods, including followers, blocking, chat management, and login.
- **Testing**: Extensive testing of `User` class functionalities, ensuring correct operation and interactions.
- **Relationships**: Directly tests `User`, verifying integration with `Chat` and file handling.

### 12. **SimpleServer Class**
- **Functionality**: Manages connections between clients, handling requests and user communication.
- **Testing**: Verified along with `ClientHandler` for client-server interactions.
- **Relationships**: Works with `ClientHandler` for connection management and interacts with `Chat` and `User` for user operations.

### 13. **User Class**
- **Functionality**: Manages user data, including username, password, profile, followers, following, blocked users, and chats. Supports data persistence across sessions.
- **Testing**: Tested through `UserTest` and `RunUserTests`, verifying follower management, blocking, and chat interactions.
- **Relationships**: Works closely with `Chat` and `Message`, and uses `InvalidFileFormatException` for file validation.

### 14. **UserInterface**
- **Functionality**: Defines methods for managing user data and interactions, such as handling followers and chats.
- **Testing**: Ensures implementing classes like `User` have the necessary functionalities.
- **Relationships**: Implemented by `User` to standardize user management.

### 15. **UserTest Class**
- **Functionality**: Tests basic `User` functionalities like managing followers, file operations, and lists.
- **Testing**: Provides basic unit tests for `User`, confirming core functionality and file persistence.
- **Relationships**: Directly tests `User`, ensuring proper data handling and interactions.

## Testing Guide
