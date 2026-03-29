package com.example.trasua.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item_toppings")
public class OrderItemTopping {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", insertable = false, updatable = false)
    private OrderItem orderItem;

    @Column(name = "topping_id")
    private Long toppingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topping_id", insertable = false, updatable = false)
    private Topping topping;

    @Column(name = "topping_name_snapshot", nullable = false)
    private String toppingNameSnapshot;

    private Integer quantity = 1;
    private BigDecimal price;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public OrderItem getOrderItem() { return orderItem; }
    public void setOrderItem(OrderItem orderItem) { this.orderItem = orderItem; }
    public Long getToppingId() { return toppingId; }
    public void setToppingId(Long toppingId) { this.toppingId = toppingId; }
    public Topping getTopping() { return topping; }
    public void setTopping(Topping topping) { this.topping = topping; }
    public String getToppingNameSnapshot() { return toppingNameSnapshot; }
    public void setToppingNameSnapshot(String toppingNameSnapshot) { this.toppingNameSnapshot = toppingNameSnapshot; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
