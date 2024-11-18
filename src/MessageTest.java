import object.Message;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Message Tests
 * <p>
 * JUnit tests for Message Class
 * <p>
 * Status: Complete
 *
 * @author Derek Mctume
 * @version 11/01/2024
 */

public class MessageTest {

    @Test
    public void testGetMessage() {

        // Test 1: returns true if message is the one passed to the constructor
        String param1 = "This totally works";
        Message testMessage1 = new Message("ID HERE", 0, param1);
        boolean result1 = testMessage1.getMessage().equals(param1);
        assertEquals("testGetMessage: Returned message should match" + 
            " the one in the constructor when they are equal",true, result1);

        // Test 2: returns false if message is NOT the one passed to the constructor
        String param2 = "This doesn't work";
        Message testMessage2 = new Message("ID HERE", 0, "Some other param");
        boolean result2 = testMessage2.getMessage().equals(param2);
        assertEquals("testGotMessage: Returned message should not " +
            "match the one in the constructor when they aren't equal",false, result2); 

    }

    @Test
    public void testGetAuthorID() {

        // Test 1: same as in testGetMessage()
        String param1 = "1347284798";
        Message testMessage1 = new Message(param1, 0, "MESSAGE HERE");
        boolean result1 = testMessage1.getAuthorID().equals(param1);
        assertEquals("testGetAuthorID: Returned authorID should match " +
            "the one in the constructor when they are equal",true, result1); 

        // Test 2: same as in testGetAuthorID()
        String param2 = "529594808";
        Message testMessage2 = new Message("10101010", 0, "MESSAGE HERE");
        boolean result2 = testMessage2.getAuthorID().equals(param2);
        assertEquals("testGetAuthorID: Returned authorID shouldn't match " +
            "the one in the constructor when they aren't equal",false, result2);
    }

    @Test
    public void testGetMessageType() {

        // Test 1: same as everything else
        int param1 = 0;
        Message testMessage1 = new Message("ID HERE", param1, "MESSAGE HERE");
        boolean result1 = (testMessage1.getMessageType() == param1);
        assertEquals("testGetMessageType: Returned message type should match " +
            "the one in the constructor when they are equal",true, result1); 


        // Test 2: same as everything else
        int param2 = 1;
        Message testMessage2 = new Message("ID HERE", 0, "MESSAGE HERE");
        boolean result2 = (testMessage2.getMessageType() == param2);
        assertEquals("testGetMessageType: Returned message type shouldn't match " +
            "the one in the constructor when they aren't equal",false, result2);
    }

    @Test
    public void testSetMessage() {

        // Test 1: if there is no image (type == 0), then
        // set the message to the param and return true
        String message1 = "Test message";
        Message testMessage1 = new Message("09090", 0, "nothing here yet");
        boolean funcResult1 = testMessage1.setMessage(message1);
        boolean correctParam1 = testMessage1.getMessage().equals(message1);

        assertEquals("testSetMessage: function should return true " +
            "upon successful mutation",true, funcResult1);
        assertEquals("testSetMessage: the message's current content was " +
            "not the one it was assigned to be changed to",true, correctParam1);

        // Test 2: return false if it's an image
        String message2 = "whatever";
        Message testMessage2 = new Message("09090909", 
            1, "nothing here again");
        boolean funcResult2 = testMessage2.setMessage(message2);
        boolean correctParam2 = testMessage2.getMessage().equals("nothing here again"); // shouldn't change

        assertEquals("testSetMessage: function should return false " +
            "upon unsuccessful mutation",false, funcResult2);
        assertEquals("testSetMessage: the message's content should not " +
            "change upon unsuccessful mutation",true, correctParam2);
    }
}