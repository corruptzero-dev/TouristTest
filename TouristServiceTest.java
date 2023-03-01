import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TouristServiceTest {
    private final Database database = mock(Database.class);
    private final TouristService service = new TouristService(database);

    @Test
    public void testBuyTicket() {
        // Valid ticket and information
        Ticket ticket = new Ticket("John Smith", 100.0);
        String information = "John Smith, 123 Main St., New York, NY";
        service.buyTicket(ticket, information);

        verify(database).purchase(ticket);
        verify(database).enterInformation(anyInt(), eq(information));
        verify(database).payOrder(anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuyTicket_NameTooLong() {
        // Invalid ticket name
        Ticket ticket = new Ticket("John Smith John Smith John Smith John Smith John Smith John Smith John Smith John Smith John Smith John Smith John Smith John Smith John Smith", 100.0);
        String information = "John Smith, 123 Main St., New York, NY";
        service.buyTicket(ticket, information);
    }

    @Test(expected = RuntimeException.class)
    public void testBuyTicket_NotEnoughFunds() {
        // Insufficient funds
        Ticket ticket = new Ticket("John Smith", 1000.0);
        String information = "John Smith, 123 Main St., New York, NY";
        doThrow(new RuntimeException("Insufficient funds")).when(database).payOrder(anyInt());
        service.buyTicket(ticket, information);
    }

    @Test
    public void testCancelOrder() {
        // Valid order ID
        int orderId = 12345; 
        service.cancelOrder(orderId);

        verify(database).cancelOrder(orderId);
    }

    @Test(expected = RuntimeException.class)
    public void testCancelOrder_InvalidOrderId() {
        // Invalid order ID
        int orderId = -1;
        doThrow(new RuntimeException("Invalid order ID")).when(database).cancelOrder(orderId);
        service.cancelOrder(orderId);
    }

    @Test
    public void testSelectSeat() {
        // Valid order ID and seat number
        int orderId = 12345; 
        int seatNumber = 5;
        doReturn(true).when(database).selectSeat(orderId, seatNumber);
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testSelectSeat_SeatNotAvailable() {
        // Invalid seat number
        int orderId = 12345; 
        int seatNumber = 100;
        doReturn(false).when(database).selectSeat(orderId, seatNumber);
        service.selectSeat(orderId, seatNumber);
    }
}
