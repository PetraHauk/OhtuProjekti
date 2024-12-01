package view.sivut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckOutTest {

    @Test
    void testCreateCheckOut() {
        CheckOut checkOut = new CheckOut();
        assertNotNull(checkOut.createCheckOut(), "CheckOut VBox should not be null");
        assertTrue(checkOut.createCheckOut().getChildren().size() > 0, "CheckOut VBox should have children");
    }
}