package order.matching.order;

import java.util.Objects;

public abstract class Order {

    private OrderSide side;
    private String orderId;
    private int quantity;
    private long timeStamp;

    public Order(OrderSide side, String orderId, int quantity, long timeStamp) {
        this.side = side;
        this.orderId = orderId;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
    }

    public OrderSide getSide() {
        return side;
    }

    public String getId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
    public boolean equals(Order other) {
        return Objects.equals(orderId, other.orderId);
    }

    public void decreaseQuantity(int qty) {
        quantity -= qty;
    }

    public abstract boolean isPriceless();

}
