package com.example.trasua.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item_toppings")
public class CartItemTopping {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_item_id", nullable = false)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", insertable = false, updatable = false)
    private CartItem cartItem;

    @Column(name = "topping_id", nullable = false)
    private Long toppingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topping_id", insertable = false, updatable = false)
    private Topping topping;

    private Integer quantity = 1;

    @Column(name = "price_at_add", nullable = false)
    private BigDecimal priceAtAdd;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCartItemId() { return cartItemId; }
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }
    public CartItem getCartItem() { return cartItem; }
    public void setCartItem(CartItem cartItem) { this.cartItem = cartItem; }
    public Long getToppingId() { return toppingId; }
    public void setToppingId(Long toppingId) { this.toppingId = toppingId; }
    public Topping getTopping() { return topping; }
    public void setTopping(Topping topping) { this.topping = topping; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPriceAtAdd() { return priceAtAdd; }
    public void setPriceAtAdd(BigDecimal priceAtAdd) { this.priceAtAdd = priceAtAdd; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
