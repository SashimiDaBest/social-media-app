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
        assertEquals(result1, true);

        // Test 2: returns false if message is NOT the one passed to the constructor 
        String param2 = "This doesn't work";
        Message testMessage2 = new Message("ID HERE", 0, "Some other param");
        boolean result2 = testMessage2.getMessage().equals(param2);
        assertEquals(result2, false);

    }

    public void testGetAuthorID() {

        // Test 1: same as in testGetMessage()
        String param1 = "1347284798";
        Message testMessage1 = new Message(param1, 0, "MESSAGE HERE");
        boolean result1 = testMessage1.getAuthorID().equals(param1);
        assertEquals(result1, true);

        // Test 2: same as in testGetAuthorID()
        String param2 = "529594808";
        Message testMessage2 = new Message("10101010", 0, "MESSAGE HERE");
        boolean result2 = testMessage2.getAuthorID().equals(param2);
        assertEquals(result2, false);
    }

    public void testGetType() {

        // Test 1: same as everything else
        int param1 = 0;
        Message testMessage1 = new Message("ID HERE", param1, "MESSAGE HERE");
        boolean result1 = (testMessage1.getType() == param1);
        assertEquals(result1, true);


        // Test 2: same as everything else 
        int param2 = 1;
        Message testMessage2 = new Message("ID HERE", 0, "MESSAGE HERE");
        boolean result2 = (testMessage1.getType() == param1);
        assertEquals(result2, false);
    }


    public void paramTestForID() {

        // 
    }

}