import org.junit. Test;
import org.junit. Ignore;
import static org.junit.Assert.assertEquals;

public class MessageTest {

    public void testEquals() {

        // NOTE: each test also has an inversion (b/c that's how equals should work)

        // Test 1: returns true when fields are equal
        Message testMessage1 = new Message("This works", "12434252");
        Message testMessage2 = new Message("This works", "12434252");
        boolean result1 = testMessage1.equals(testMessage2);
        assertEquals(result1, true);

        boolean invertedResult1 = testMessage2.equals(testMessage1);
        assertEquals(invertedResult1, true);

        // Test 2: returns false when messages (as in the String) are not equal
        Message testMessage3 = new Message("This works", "12434252");
        Message testMessage4 = new Message("This doesnt work", "12434252");
        boolean result2 = testMessage3.equals(testMessage4);
        assertEquals(result2, false);

        boolean invertedResult2 = testMessage4.equals(testMessage3);
        assertEquals(invertedResult2, false);

        // Test 3: returns false when IDs are not equal
        Message testMessage5 = new Message("This works", "12434252");
        Message testMessage6 = new Message("This work", "0202020");
        boolean result3 = testMessage5.equals(testMessage6);
        assertEquals(result3, false);

        boolean invertedResult3 = testMessage6.equals(testMessage5);
        assertEquals(invertedResult3, false);

        // Test 4: returns false when BOTH fields are not equal
        Message testMessage7 = new Message("There's quite a few tests...", "987654321");
        Message testMessage8 = new Message("I know!", "123456789");
        boolean result4 = testMessage7.equals(testMessage8);
        assertEquals(result4, false);

        boolean invertedResult4 = testMessage8.equals(testMessage7);
        assertEquals(invertedResult4, false);

        // Following tests are only for messages that contain images


        // Test 5: returns true when fields are equal
        Message imageMessage1 = new Message("This message has an image", "123456", true);
        Message imageMessage2 = new Message("This message has an image", "123456", true);
        boolean result5 = imageMessage1.equals(imageMessage2);
        assertEquals(result5, true);

        boolean invertedResult5 = imageMessage2.equals(imageMessage1);
        assertEquals(invertedResult5, true);

        // Test 6: returns false when message Strings are not equal
        Message imageMessage3 = new Message("Message without an image", "123456", true);
        Message imageMessage4 = new Message("Message with an image", "123456", true);
        boolean result6 = imageMessage3.equals(imageMessage4);
        assertEquals(result6, false);

        boolean invertedResult6 = imageMessage4.equals(imageMessage3);
        assertEquals(invertedResult6, false);

        // Test 7: returns false when IDs are not equal
        Message imageMessage5 = new Message("Same message, diff Id", "7", true);
        Message imageMessage6 = new Message("Same message, diff Id", "42000000000", true);
        boolean result7 = imageMessage5.equals(imageMessage6);
        assertEquals(result7, false);

        boolean invertedResult7 = imageMessage6.equals(imageMessage5);
        assertEquals(invertedResult7, false);

        // Test 8: returns false when BOTH message and ID are not equal (but both still have images)
        Message imageMessage7 = new Message("Hi :)", "11", true);
        Message imageMessage8 = new Message("BYE!!!", "97", true);
        boolean result8 = imageMessage7.equals(imageMessage8);
        assertEquals(result8, false);

        boolean invertedResult8 = imageMessage8.equals(imageMessage7);
        assertEquals(invertedResult8, false);

        // Test 9: if a message has an image and the other message doesn't, they shouldn't be equal
        Message imageMessage9 = new Message("Same message", "144", false);
        Message imageMessage10 = new Message("Same message", "144", true);
        boolean result9 = imageMessage9.equals(imageMessage10);
        assertEquals(result9, false);

        boolean invertedResult9 = imageMessage10.equals(imageMessage9);
        assertEquals(invertedResult9, false);

        // Test 10: finally, all message should equal themselves
        Message testMessage9 = new Message("This isn't even the last test", "125125");
        boolean result10 = testMessage9.equals(testMessage9);
        assertEquals(result10, true);

        // Test 11: same as Test 10, except with image parameters
        Message imageMessage11 = new Message("I'm out of ideas", "13131313", false);
        boolean result11 = imageMessage11.equals(imageMessage11);
        assertEquals(result11, true);

        // Test 12: above should also work even if isImage == true
        Message imageMessage12 = new Message("I don't know", "089898", true);
        boolean result12 = imageMessage12.equals(imageMessage12);
        assertEquals(result12, true);

    }

    public void testGetMessage() {

        // Test 1: returns true if message is the one passed to the constructor
        String message1 = "This totally works";
        Message testMessage1 = new Message(message1, "9008090", false);
        String result1 = testMessage1.getMessage();
        assertEquals(result1, message1);


        // Test 2: returns false if message is the NOT the one passed to the constructor 
        String message2 = "This doesn't work";
        Message testMessage2 = new Message("This totally works", "909090808", false);
        boolean result2 = testMessage2.getMessage().equals(message2);
        assertEquals(result2, false);


    }
}