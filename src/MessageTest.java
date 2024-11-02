import org.junit. Test;
import org.junit. Ignore;
import static org.junit.Assert.assertEquals;

/**
 * Social Media App - Message Tests
 *
 * JUnit tests for Message Class
 *
 * Status: In Progress
 *
 * @author derek mctume
 *
 * @version 11/01/2024
 *
 */

public class MessageTest {


    public void testGetMessage() {

        // Test 1: returns true if message is the one passed to the constructor
        String param1 = "This totally works";
        Message testMessage1 = new Message("ID HERE", 0, param1);
        boolean result1 = testMessage1.getMessage().equals(param1);
        assertEquals(true, result1);

        // Test 2: returns false if message is NOT the one passed to the constructor 
        String param2 = "This doesn't work";
        Message testMessage2 = new Message("ID HERE", 0, "Some other param");
        boolean result2 = testMessage2.getMessage().equals(param2);
        assertEquals(false, result2); 

    }

    public void testGetAuthorID() {

        // Test 1: same as in testGetMessage()
        String param1 = "1347284798";
        Message testMessage1 = new Message(param1, 0, "MESSAGE HERE");
        boolean result1 = testMessage1.getAuthorID().equals(param1);
        assertEquals(true, result1); 

        // Test 2: same as in testGetAuthorID()
        String param2 = "529594808";
        Message testMessage2 = new Message("10101010", 0, "MESSAGE HERE");
        boolean result2 = testMessage2.getAuthorID().equals(param2);
        assertEquals(false, result2);
    }

    public void testGetMessageType() {

        // Test 1: same as everything else
        int param1 = 0;
        Message testMessage1 = new Message("ID HERE", param1, "MESSAGE HERE");
        boolean result1 = (testMessage1.getMessageType() == param1);
        assertEquals(true, result1); 


        // Test 2: same as everything else 
        int param2 = 1;
        Message testMessage2 = new Message("ID HERE", 0, "MESSAGE HERE");
        boolean result2 = (testMessage2.getMessageType() == param2);
        assertEquals(false, result2);
    }

    public void testSetMessage() {

        // Test 1: if there is no image (type == 0), then 
        // set the message to the param and return true
        String message1 = "Test message";
        Message testMessage1 = new Message("09090", 0, "nothing here yet");
        boolean funcResult1 = testMessage1.setMessage(message1);
        boolean correctParam1 = testMessage1.getMessage().equals(message1);

        assertEquals(true, funcResult1);
        assertEquals(true, correctParam1);

        // Test 2: return false if it's an image
        String message2 = "whatever";
        Message testMessage2 = new Message("09090909", 1, "nothing here again");
        boolean funcResult2 = testMessage2.setMessage(message2);
        boolean correctParam2 = testMessage2.getMessage().equals("nothing here again"); // shouldn't change

        assertEquals(false, funcResult2);
        assertEquals(true, correctParam2);
    }


    public static void main(String[] args) {
        
        MessageTest runTests = new MessageTest();
        runTests.testGetMessage();
        runTests.testGetAuthorID();
        runTests.testGetMessageType();
        runTests.testSetMessage();
    }
}