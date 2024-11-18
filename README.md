
# Social Media App
CS 180 Team Project

## Table of Contents
- [Simplified Directory Structure](#simplified-directory-structure)
- [Compilation and Execution Instructions](#compilation-and-execution-instructions)
- [Submission Record](#submission-record)
- [Documentation](#documentation)
    - [clientPageOperation](#clientpageoperation)
    - [exception](#exception)
    - [object](#object)
    - [serverPageOperation](#serverpageoperation)
    - [uiPage](#uipage)
- [Testing and Verification Instructions](#testing-and-verification-instructions)

## Simplified Directory Structure
```plaintext
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


## Documentation
A comprehensive overview of each class, covering its functionality, the testing conducted to ensure it operates correctly, and its interactions with other classes within the project.
1. **ClientHandler.java**

2. **MessageTest.java**
- **Functionality**:
  The `MessageTest` class contains JUnit tests to verify the functionality of the `Message` class. It tests methods that retrieve message details (such as author ID, message type, and content) and ensure message content updates are handled correctly based on the message type.
- **Testing**:
    - **testGetMessage**:
        - Confirms that the `getMessage` method correctly returns the message content.
        - Includes tests to ensure that the content matches or does not match as expected.
    - **testGetAuthorID**:
        - Verifies that the `getAuthorID` method returns the correct author ID.
        - Ensures that the method does not return incorrect author IDs.
    - **testGetMessageType**:
        - Checks if the `getMessageType` method returns the correct message type.
        - Ensures that incorrect message types are not returned.
    - **testSetMessage**:
        - Validates the `setMessage` method, confirming it updates the message content if the message type is text (0) and returns `true`.
        - Tests that the method does not update the message if the type is non-text and returns `false`.
- **Relationships**: The `MessageTest` class is responsible for testing the `Message` class. It does not interact with other classes directly but ensures that the `Message` class functions correctly within the larger application.

3. **RunChatTests.java**
- **Functionality**: The RunChatTests class is a JUnit test suite that rigorously tests the functionality of the Chat class. It includes various test methods to ensure the correct implementation of chat operations, such as constructing chats, adding messages, editing messages, and deleting messages. The tests also validate the proper handling of file-based data persistence and exceptions related to invalid file formats.
- **Testing**:
  - **`testNoReadConstructor`**: Verifies that a chat created without reading from a file correctly writes data to a new file and compares the contents against expected values.
  - **`testReadConstructor`**: Tests the ability of the `Chat` class to instantiate from a file with valid data. It also checks for the correct handling of corrupt data by asserting that `InvalidFileFormatException` is thrown when necessary.
  - **`testAddMessage`**: Checks that a new message is added to the `messageList` and written to the file accurately. It also verifies that message type and contents match the expected values in the file.
  - **`testDeleteMessage`**: Validates the deletion of a message from the `messageList` and ensures that the changes are correctly written to the file, resulting in an empty message list.
  - **`testEditMessage`**: Ensures that editing a message by its author properly updates the message in the `messageList` and writes the changes to the file.
- **Relationships**: The `RunChatTests` class tests the `Chat` class, which is a core component of the social media application. It ensures that the methods related to message handling and data persistence in the `Chat` class are functioning correctly. The tests also involve interactions with the `Message` class to validate the integrity of chat messages. Additionally, file handling and exceptions like `InvalidFileFormatException` are tested to ensure robust error handling.

4. **RunUserTests.java**
- **Functionality**:
  `RunUserTests` contains JUnit tests that evaluate the `User` class functionality. The tests cover the behavior of methods related to user creation, attribute setting, following and blocking users, chat management, and file-based data persistence.
- **Testing**:
    - **User Creation**: The constructor tests verify the creation of `User` objects, ensuring that attributes such as username, password, and user ID are properly initialized and that a corresponding data file is created.
    - **Mutator Methods**: Tests like `testSetUsername`, `testSetPassword`, `testSetProfilePic`, and `testSetAccountType` validate that changes to user attributes are reflected correctly.
    - **User Relationships**:
        - Tests for adding and removing followers, following users, and blocking/unblocking ensure that lists are updated as expected.
        - Methods like `addFollower`, `addBlock`, `deleteFollower`, and `deleteBlock` are verified for correctness.
    - **Chat Management**:
        - Methods `addChat` and `createChat` are tested to ensure chats are appropriately linked to users.
    - **Utility Methods**:
        - `findUser` and `userNameValidation` are tested for correct behavior when searching for users and validating usernames.
    - **File Persistence**: After each test, created user and chat files are cleaned up, and the `UserIDList` and `chatIDList` files are reset.
- **Relationships**:
    - Relies on the `User` and `Chat` classes to test user-to-user interactions and chat functionalities.
    - Tests interactions between multiple `User` objects to simulate a realistic social media environment.
    - Uses file operations to ensure user and chat data are correctly written and retrieved.

5. **ServerInterface.java**


6. **SimpleServer.java**


7. **SimpleServerTest.java**


8. **UserTest.java**
- **Functionality**:
  The `UserTest` class provides a comprehensive suite of JUnit tests for the `User` class. It tests the constructors, mutators, and various methods used in user management, such as adding followers, blocking users, creating chats, and handling authentication. The tests ensure that user data is correctly persisted in files, attributes are properly assigned and updated, and edge cases are managed.
- **Testing**:
    - **Constructor Tests**:
        - `testSecondUserConstructor`: Verifies proper initialization of a `User` object, checks that all attributes are correctly set, and confirms that the corresponding user data file is created with accurate contents.
    - **Mutator Tests**:
        - `testSetUsername`, `testSetPassword`, `testSetProfilePic`, `testSetAccountType`, `testSetUserID`: Ensure that setting these attributes updates the user object and the user data file appropriately.
    - **Method Tests**:
        - `testAddBlock`, `testDeleteBlock`: Test the ability to block and unblock users, ensuring the user’s block list is updated and the function returns expected values.
        - `testAddChat`, `testCreateChat`: Check if chat IDs are correctly added to the user’s chat list, with validation to prevent duplicates.
        - `testHasLogin`: Verifies the login functionality by matching the user’s credentials with data in the `UserIDList` file.
        - `testCreateNewUser`: Confirms that new user entries are written to `UserIDList.txt` with correct information.
        - `testSendText`: Ensures messages are successfully sent to existing chats and handles exceptions for non-existent chats.
- **Relationships**:
    - Uses the `User` class to instantiate user objects and test their behaviors.
    - Tests interactions with the `Chat` class to validate chat-related functionalities.
    - Implements exception handling using `NoChatFoundException` for scenarios where chats are not found.

### clientPageOperation
1. **FeedPageClient.java** 
2. **OtherPageClient.java**
3. **UserPageClient.java**
4. **WelcomePageClient.java**

### exception
1. **InvalidCreateAccountException.java**
- **Functionality**: A custom exception designed to handle errors during account creation, such as invalid passwords or other fields that fail to meet predefined validation requirements. It provides meaningful error messages, enhancing the debugging and user experience.
- **Testing**: Verified through unit tests by passing various invalid inputs (e.g., short passwords, null fields) and confirming that the exception is thrown with the correct message. Edge cases, such as empty strings, were also tested to ensure comprehensive coverage.
- **Relationships**: Primarily used in the `User` class for validation during account creation. It may also be referenced by UI classes to display error messages and by server logic to reject invalid requests. The exception simplifies error handling and promotes clear communication of issues.

2. **InvalidFileFormatException.java**
- **Functionality**: A custom exception class used to handle errors related to file format mismatches. It ensures that files being read or processed conform to a specified structure and throws this exception when the format is incorrect, preventing the program from using invalid or corrupted data.
- **Testing**: Verified through unit tests by attempting to load files with incorrect or unexpected formats, such as missing fields or invalid delimiters. Tests ensured that the exception is thrown and that the error message correctly identifies the format issue.
- **Relationships**: Used in classes like `Chat` and `User` where file operations are crucial. It helps maintain data integrity by catching and handling format errors before they can cause broader application failures, thus providing a robust mechanism for file validation.

3. **NoChatFoundException.java**
- **Functionality**: A custom exception class that handles scenarios where a requested chat cannot be found. This exception is thrown when operations are attempted on a chat ID that does not exist, providing clear feedback on the error and allowing for more graceful error handling within the application.
- **Testing**: Verified through unit tests by attempting to access non-existent chat IDs and ensuring that the exception is thrown with an appropriate message. The tests also check that the exception does not disrupt the application flow and that error handling mechanisms are activated as expected.
- **Relationships**: Used in classes such as `Chat` and `User` where chat-related operations are performed. This exception ensures that methods depending on the presence of valid chats can handle cases where chats are missing, enhancing the robustness of chat management and communication features within the social media application.

### object
1. **Chat.java**
- **Functionality**: The `Chat` class manages chat-related operations within the social media application. It is responsible for maintaining chat member lists, message lists, and persisting chat data in files. The class provides methods for creating new chats, reading chat data from a file, adding, editing, and deleting messages, and generating unique chat IDs using a counter. The file-based persistence ensures that chat data is saved and can be reloaded when needed.
- **Testing**: The class has been tested to ensure correct initialization from files and proper functionality for message handling. Unit tests have been performed to validate chat creation, message addition, message editing, and message deletion. Additional tests ensure that chat data is written correctly to files and that unique chat IDs are generated consistently. Error handling for invalid file formats has also been verified.
- **Relationships**: The `Chat` class implements the `ChatInterface`, ensuring a standardized approach to chat operations. It interacts with the `Message` class for message management and uses custom exceptions such as `InvalidFileFormatException` to handle errors. The `Chat` class is often used in conjunction with `User` and other classes that require chat functionality, serving as a core component of the communication system in the application.

2. **ChatInterface.java**
- **Functionality**: The `ChatInterface` defines the required methods for chat operations in the social media application. It includes methods for data management, such as writing chat data to storage, handling messages, and managing chat members. Additionally, it specifies mechanisms for creating unique chat IDs and retrieving information about the chat.
- **Testing**: The `ChatInterface` itself is not directly tested, but it serves as a contract for implementing classes. The `Chat` class, which implements this interface, has been thoroughly tested to ensure that all methods function correctly, including message handling and data persistence.
- **Relationships**: The `ChatInterface` is implemented by the `Chat` class, which provides the concrete functionality for chat operations. It ensures that any class implementing this interface adheres to a standard structure, making the chat functionality consistent across the application. The interface works closely with the `Message` class and data storage mechanisms.

3. **Message.java**
- **Functionality**:
  The `Message` class represents an individual message in a chat. It contains details such as the author’s ID, the type of the message (text or image pathway), and the content of the message. The class provides methods for accessing and modifying message attributes. Specifically, it allows text messages to be edited while restricting modifications to image messages. The `equals` method is overridden to check for equality based on the author ID, message type, and content.
- **Testing**:
  Testing should verify the following:
    - Creation of `Message` instances with valid author ID, message type, and content.
    - Correct retrieval of message details using `getAuthorID`, `getMessageType`, and `getMessage`.
    - Proper functioning of `setMessage`, ensuring only text messages can be edited, and image messages remain unchangeable.
    - Correct behavior of the `equals` method, confirming equality based on the specified criteria.
- **Relationships**: The `Message` class is used by the `Chat` class to manage messages within a chat. Each `Message` instance provides the content and type information necessary for chat operations, including message addition, deletion, and editing. It also works with file-based data persistence when `Chat` writes or reads messages to and from files.

4. **MessageInterface.java**
- **Functionality**:
  The `MessageInterface` defines the structure for message-related operations within the social media application. It specifies methods for retrieving and modifying the properties of a message, including the author ID, message type, and message content. The interface ensures that any implementing class provides consistent behavior for message management, such as retrieving the content and author ID and determining whether a message can be updated.
- **Testing**:
  Testing for classes implementing `MessageInterface` should verify:
    - Correct implementation of `getAuthorID`, `getMessageType`, and `getMessage` methods to ensure accurate data retrieval.
    - The `setMessage` method should be tested to confirm that updates to message content are only allowed when the message type permits it.
    - Consistent behavior across different types of messages (e.g., text vs. non-text) when invoking these methods.
- **Relationships**: The `MessageInterface` is implemented by the `Message` class, which provides concrete behavior for message handling. It serves as a contract that the `Message` class must adhere to, ensuring that the application has a unified approach to managing messages.

5. **User.java**
- **Functionality**:
  The `User` class represents a user within the social media application. It manages user information such as username, user ID, profile picture, followers, following, blocked users, and associated chats. The class provides methods to handle user data operations like adding followers, creating chats, updating profile information, and sending messages. The class also includes mechanisms for reading and writing user data to files, and a method for handling images.
- **Testing**:
    - Extensive testing was performed on methods that manage user data (e.g., `addFollower`, `deleteFollower`, `addChat`, and `deleteChat`), ensuring data persistence and correct functionality.
    - Methods responsible for file operations and user data validation were also tested for robustness, including edge cases like invalid usernames and passwords.
    - Multi-threaded scenarios were tested to ensure thread safety of synchronized methods and lock-based operations.
- **Relationships**:
    - The `User` class implements the `UserInterface` and interacts with the `Chat` and `Message` classes for chat and messaging functionalities.
    - It uses exceptions like `InvalidCreateAccountException` and `NoChatFoundException` for error handling.
    - The class relies on Java I/O for file-based persistence and utilizes `BufferedImage` for profile picture management.

6. **UserInterface.java**
- **Functionality**:
  The `UserInterface` defines the essential behaviors for a user in the social media application. It outlines methods for managing user properties such as username, profile picture, followers, following, blocked users, and chat IDs. It also includes methods for sending messages, validating login credentials, and handling user interactions, like creating and managing chats.
- **Testing**:
    - Testing involves implementing these methods in a `User` class and verifying each method's functionality with various test cases, especially file operations and data validation.
    - Authentication, data persistence, and access control are thoroughly tested to ensure the correct behavior under different conditions, including edge cases.
- **Relationships**:
    - `UserInterface` is implemented by the `User` class, which uses this interface to enforce a consistent structure for user-related operations.
    - The interface defines interactions with `Chat` and `Message` classes, and it utilizes exceptions such as `NoChatFoundException` for error handling.

### serverPageOperation
1. **FeedPageServer.java**

2. **OtherPageServer.java**

3. **UserPageServer.java**

4. **WelcomePageServer.java**


### uiPage (Incomplete)


## Testing and Verification Instructions
