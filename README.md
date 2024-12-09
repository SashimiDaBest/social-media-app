
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
    - [page](#uipage)
- [Testing and Verification Instructions](#testing-and-verification-instructions)

## Simplified Directory Structure
```plaintext
src/ 
|
├── common/                        # Shared utilities and reusable components
│   ├── PageManager.java           # Manages navigation between pages
│   ├── ProfilePictureDialog.java  # Dialog for displaying profile pictures
│   ├── RoundedButton.java         # Custom button with animation effects
│   └── Writer.java                # Handles server communication (reading/writing)
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
├── page/                        # UI-related classes and client-side operations
│   ├── CreateUserPage.java        # Page for user creation
│   ├── FeedViewPage.java          # Page for displaying the user's feed
│   ├── OtherProfilePage.java      # Page for viewing other users' profiles
│   ├── UserProfilePage.java       # Page for viewing the user's own profile 
│   └── WelcomePage.java           # Welcome page for the application creation
│
├── serverPageOperation/           # Server-side operations and functionality 
│   ├── FeedPageServer.java        # Handles feed page operations on the server
│   ├── OtherPageServer.java       # Handles other user page operations on the server
│   ├── UserPageServer.java        # Handles user profile operations on the server
│   └── WelcomePageServer.java     # Handles welcome page operations on the server
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
- **Functionality**:
   The ClientHandler class manages individual client connections for a social media application. It handles communication between the client and the server, using input and output streams to exchange messages. Additionally, it sets up and manages the user interface (UI) using Swing components. The class implements Runnable, enabling each client connection to run on a separate thread, which supports concurrent communication with multiple clients. The user interface includes pages such as the welcome screen, user profile, and feed view, allowing seamless navigation and interaction within the application.
- **Testing**:
  - Verified the successful establishment of client-server connections and the proper initialization of input/output streams.
  - Checked that GUI components are correctly created, displayed, and responsive to user interactions.
  - Ensured that user input is appropriately processed and that messages are accurately sent and received from the server.
  - Tested edge cases, such as handling invalid usernames and passwords, to ensure robust error handling with proper feedback.
  - Conducted multi-threaded testing to confirm the correct management of concurrent client connections, ensuring no race conditions or deadlocks occur.
- **Relationships**:
  - Implements Runnable to support multi-threaded client handling, allowing the server to communicate with multiple clients simultaneously.
  - Uses BufferedReader and BufferedWriter to facilitate message exchange between the client and server.
  - Integrates with WelcomePageClient to manage welcome page functionality and connects with other UI components for a seamless user experience.
  - Employs Swing (JFrame, JPanel, CardLayout) from the javax.swing and java.awt packages to construct and manage the graphical user interface.
  - Collaborates with various UI pages (WelcomePage, CreateUserPage, FeedViewPage, UserProfilePage, and OtherProfilePage) to enable user navigation and interactions within the application.

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
- **Functionality**: The ServerInterface defines the essential operations that a simple server must implement. It provides a contract for starting and stopping the server, as well as retrieving lists of users and chats managed by the server. By defining these methods, the interface ensures that any class implementing it will provide consistent and structured behavior for server operations. The interface is useful for promoting code reusability and standardizing how servers are managed within the application.
- **Relationships**:
  - Any class implementing ServerInterface must provide concrete implementations for all declared methods.
  - The interface ensures that implementing classes have mechanisms for managing User and Chat objects, facilitating seamless user and chat handling.
  - It interacts with the User and Chat classes, which represent user entities and chat sessions respectively, providing a standardized way to access these objects within server implementations.

6. **SimpleServer.java**
- **Functionality**: The SimpleServer class provides a basic server implementation that listens for client connections on a specified port and delegates request handling to the WelcomePageServer. It uses input and output streams for communication with clients and supports loading user and chat data from files. The server is designed to handle multiple client connections concurrently by leveraging the Runnable interface, allowing each connection to run on a separate thread. The class also includes mechanisms for managing resources and ensuring proper closure of sockets and streams.
- **Testing**:
  - Verified that the server correctly initializes by loading User and Chat objects from data files in the designated directory.
  - Ensured that client connections are accepted and handled correctly, with requests being processed by the WelcomePageServer.
  - Checked for proper error handling and resource management, especially for closing sockets and streams to avoid memory leaks.
  - Conducted stress testing to ensure the server can manage multiple concurrent connections without performance degradation.
  - Validated that invalid or corrupt chat files are handled gracefully, using appropriate exception handling mechanisms.
- **Relationships**:
  - Implements Runnable to support multi-threaded client handling, allowing the server to communicate with multiple clients simultaneously.
  - Utilizes the WelcomePageServer class to manage initial client interactions, such as user sign-in and sign-up.
  - Interacts with the User and Chat classes to load and manage data from files, converting them into objects for in-memory storage.
  - Uses Java networking (ServerSocket and Socket) and I/O classes (BufferedReader, BufferedWriter) to establish connections and facilitate communication.
  - Relies on exception handling classes like InvalidFileFormatException to manage errors when loading data.

7. **SimpleServerTest.java**
- **Functionality**: The SimpleServerTest class provides unit tests for the SimpleServer class. Its main purpose is to validate that the SimpleServer constructor correctly processes files from the data directory and converts them into User and Chat objects. The test checks that every file in the data directory corresponds to an appropriate User or Chat object in the server’s internal data structures. The class ensures data integrity and verifies that no files are overlooked during the object creation process.
- **Testing**:
  - testServerConstructor(): This unit test:
  - Checks that each file in the data directory is successfully converted into a User or Chat object.
  - Compares the total number of files in the directory with the number of objects created and stored in the SimpleServer.
  - Iterates through each file, extracts the file name, and verifies that it matches an ID of a User or Chat object.
  - Uses assertEquals to ensure the total number of objects matches the number of files, confirming that all data has been processed correctly.
  - The test is designed to handle potential issues with file I/O gracefully, throwing a RuntimeException if an IOException occurs.
- **Relationships**:
  - Uses SimpleServer to test the constructor's ability to populate the user and chat lists from files.
  - Relies on the User and Chat classes to verify that data from files is appropriately converted into objects.
  - Integrates with JUnit for testing, using assertions to validate the correctness of the server's initialization process.
  - The main method allows for manual testing, providing a way to run the test independently of the JUnit framework.

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
- **Functionality**: The `FeedPageClient` class serves as the client-side handler for interacting with the feed page of the application. It facilitates user actions such as browsing through content, selecting specific users for interaction, or navigating to other parts of the application. It manages user input, sends commands to the server, and processes responses to display appropriate content to the user.
- **Testing**:
  - Verified that all navigation options (e.g., viewing users, returning to the main menu) correctly communicate with the server and handle the responses as expected.
  - Ensured edge cases, such as invalid inputs or server errors, are gracefully managed.
- **Relationships**:
  - Communicates with `FeedPageServer` to receive and display feed content.
  - Interacts with `OtherPageClient` and `UserPageClient` for user navigation.

2. **OtherPageClient.java**
- **Functionality**: The `OtherPageClient` class is responsible for handling the client-side operations of viewing and interacting with another user’s profile. It provides options for actions like following/unfollowing, blocking/unblocking, and viewing the selected user’s follower or following list. The class captures user input, sends requests to the server, and displays the results.
- **Testing**:
  - Tested the proper sending of requests (e.g., follow/unfollow) and the correct handling of server responses.
  - Verified that invalid or unexpected input is managed effectively and does not crash the client application.
- **Relationships**:
  - Works with `OtherPageServer` for server-side profile operations.
  - Interacts with `UserPageClient` for redirecting back to the user’s profile or the feed page.

3. **UserPageClient.java**
- **Functionality**: The `UserPageClient` class manages the client-side interactions for the user’s own profile page. It allows the user to view their account details, manage their list of followers, following, and blocked users, and navigate to other sections such as the feed or other user profiles. The class processes input from the user, sends appropriate requests to the server, and displays the information received.
- **Testing**:
  - Verified that profile data is accurately retrieved from the server and displayed.
  - Checked edge cases like empty follower or blocked lists to ensure correct behavior.
  - Ensured navigation between sections (e.g., viewing followers, going back to the feed) works seamlessly.
- **Relationships**:
  - Communicates with `UserPageServer` for account and profile management.
  - Redirects to `FeedPageClient` and `OtherPageClient` based on user choices.

4. **WelcomePageClient.java**
- **Functionality**: The `WelcomePageClient` class handles the client-side operations of the welcome page, which includes sign-in and sign-up functionalities. It manages user input for login credentials, communicates with the server to validate or create accounts, and redirects the user to the feed page upon successful login or account creation.
- **Testing**:
  - Verified sign-in and sign-up flows to ensure proper communication with `WelcomePageServer`.
  - Tested various scenarios, such as invalid credentials or taken usernames, to confirm appropriate error handling and messaging.
- **Relationships**:
  - Works closely with `WelcomePageServer` for authentication and account creation.
  - Redirects to `FeedPageClient` once the user is authenticated.

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
- **Functionality**: The FeedPageServer class handles server-side operations for the feed page. It provides features such as listing available users, facilitating user selection for interaction, and managing navigation requests from the client. The class ensures smooth communication between the client and the server for feed-related activities.
- **Testing**:
  - Checked that the server correctly lists available users and processes client commands.
  - Verified that communication errors are caught and managed gracefully.
- **Relationships**:
  - Communicates with FeedPageClient to deliver feed content and handle user selections.
  - Redirects to OtherPageServer and UserPageServer for user profile interactions.

2. **OtherPageServer.java**
- **Functionality**: The OtherPageServer class manages server-side operations for viewing and interacting with other users’ profiles. It handles actions such as following/unfollowing, blocking/unblocking, and displaying the lists of followers and following users. The class reads and writes user data, ensuring proper updates are made based on client input.
- **Testing**:
  - Tested follow/unfollow and block/unblock functionalities to ensure data consistency.
  - Verified that the correct data is sent to the client, and invalid requests are properly handled.
- **Relationships**:
  - Communicates with OtherPageClient to handle profile interactions.
  - Uses User and Chat objects for managing user relationships and messaging.

3. **UserPageServer.java**
- **Functionality**: The UserPageServer class handles server-side operations related to the user’s profile page. It manages tasks such as displaying user details, handling interactions with followers, following, and blocked lists, and facilitating navigation to other pages. The class ensures user data is correctly processed and updated based on client commands.
- **Testing**:
  - Verified that all user details (e.g., followers, profile picture updates) are accurately transmitted and displayed.
  - Checked that navigation and data management (e.g., blocking/unblocking users) function as expected.
- **Relationships**:
  - Communicates with UserPageClient for displaying and updating user account details.
  - Works with OtherPageServer for navigating to other user profiles.

4. **WelcomePageServer.java**
- **Functionality**: The WelcomePageServer class manages server-side operations for the welcome page, including user authentication (sign-in) and account creation (sign-up). It validates user credentials, creates new accounts, and communicates back to the client with success or error messages. Upon successful sign-in or sign-up, it redirects the user to the feed page.
- **Testing**:
 - Checked the sign-in and sign-up processes, ensuring correct validation and data handling.
 - Tested error handling for scenarios like duplicate usernames and invalid credentials.
- **Relationships**:
  - Interacts with WelcomePageClient for user authentication.
  - Redirects to FeedPageServer once the user is authenticated or a new account is created.
  - Uses User and Chat objects to manage user data and interactions.

### page (Incomplete)

## Testing and Verification Instructions
### **_Running Test Cases_**
Sample data is provided for physically testing this program, but the test cases are not compatible with this sample data (as I/O is tested manually). When you have finished testing the functionality of the chat by following the instructions below, you should delete all of the files within the “Sample Test Folder” and then run the test cases, and they should all pass.
### _**Test Welcome Page**_
1. **Signing in with an existing user successfully**
- Launch the program
- Type in “EchoHorizon” for username
- Type in “gj50v75nLU” for password
- You should be welcomed into the feed page
2. **Test creating a new account from initial welcome page successfully**
- Press the Sign Up button at the bottom
- Type in a new username (adhere to the printed restrictions)
- Type in a new password (can ignore the printed restrictions except for including semicolons or having blank passwords)
- You should be welcomed into the feed page
3. Failing to log in / sign up
- If you make any intentional errors in signing up or logging in (invalid username, password, etc), it should prompt you with an error JOptionPane explaining what you did wrong and prompting you to try again.
### **_Test Feed Page_**
When signed in, you will be entered into the Feed page. 

1. **Test creating a new chat with selected users**
- Search for a specific user or a generic text like “e” in the search bar to access a list of users (press the magnifying glass to search what you typed in)
- Select the user you’d like to add
- You should see that user appear in your list of selected users
- When you’ve added all the users you want to chat with, press the “Add Selected to Chat” button  (the plus sign next to the selection list) and the chat should be created.
- click on the UserPage, the return to the FeedPage by pressing “Feed” to see the new chat
2. **Test editing the selection list**
- When users are selected from the dropdown, you can delete the last user you just added (the back button) or clear the entire list (the eraser button)
3. **Test opening an existing chat**
- You can select any of the buttons on the left with chat ID numbers on them to immediately switch between your participating chats.
4. **Test sending, editing, and deleting messages**
- Type anything into the chat box
- Press “send” (the paper airplane) to send that text into the chat, “edit” (the pencil) to replace your most previous message text with the text you typed, or “delete” (the trash can) to delete your most recent message
5. **Accessing your own and other profiles**
- You can click the “User Page” (the profile icon) button in the top left to navigate to your own profile
- When you select a user to add them to a chat, you can alternatively click the new button that appears with their name on it to view their profile
### **_Test User Page_**
Before testing the user page, navigate to the profile page. The instruction below uses the user EchoHorizon account (the information of which is in U_0003.txt). That said, testing with any user should still work.
Note: since the actual program will only display the option to navigate to a certain user after viewing the followers, “followings”, and blocked users, testing whether the program correctly extracts and displays these people’s information and profile page should indicate that the program ran successfully.

1. **Test Storing Image**
- Click the user profile icon in the top left
- Use a file path from the tester's computer. The tester’s image appearing in the sample test folder after following the prompt should indicate successful writing of the image file on the server side.
2. **Test Opening Other Users’ Pages**
- The usernames on the screen should show your follower, followed, and blocked lists
- Press any of the buttons to open their pages
- Press “Feed” to return back to the feed page
3. **Test Switching Back to App Feed Page**
- press “Feed” at the bottom of the page
 - this should return you back to the main Feed page from before
4. **Test Exiting App**
  - Close the window to exit the program and logout 
 - Program should terminate

### **_Test Other Page_**
To View another User’s profile, in the Feed page, enter 4.
Enter “PixelTrail” to select PixelTrail’s account to view
1. **Testing unfollowing/following the other user AKA the profile being viewed**
- Press the “Follow” button to follow the user
  - If your account is not following the other user, then your account will start to follow the other user. Your account’s following list will add PixelTrail’s ID to itself, and PixelTrail’s follower list will add your account’s ID to itself.   
- If your account is following the user, then your account will stop following the user. Your account’s following list will remove PixelTrail’s ID and PixelTrail’s follower list will remove your account’s ID from itself. 
2. **Testing unblocking/blocking the other user AKA the profile being viewed**
- Enter 2 in order to unblock/block the other user
  - If PixelTrail is blocked by the current user (your account), then PixelTrail will be unblocked by the current user. Your account’s blocked list will remove PixelTrail’s id from itself.
    - If PixelTrail is not blocked by the current user (your account), then PixelTrail will become blocked by the current user. Your account’s blocked list will add PixelTrail’s ID to itself. 
3. **Testing viewing followers of the other user AKA the profile being viewed**
  - If the other User is a private account, but follows the current user, then you’ll be able to see their followers. If the other user is a private account, and doesn’t follow the current user, then nothing will be displayed. If the other user is public, then the followers will be displayed regardless.
    - In this case, PixelTrail is a private account, and DOES follow the current user. Therefore, PixelTrail’s follower list should be displayed.
4. **Testing viewing the following of the other user AKA the profile being viewed**
     - If the other user is private and the current user follows the other user, then the following of the other user will be displayed. If the other user is private, and the current user doesn’t follow the other user, then the following list will not be displayed. If the profile is public then the following list will be displayed regardless if the current user follows the other user or not.
       - In this case, PixelTrail is a private account. Whether or not the current user follows PixelTrail should be known to the TA operating the program.
5. **Testing returning to the feed**
   - Press “Feed” at the bottom in order to return to the Feed page
     - Current user should be returned to the Feed page
6. **Exiting the program**
   - Close the window to exit the program and logout 
     - Program should terminate
