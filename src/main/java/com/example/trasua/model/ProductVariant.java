package com.example.trasua.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(name = "size_id", nullable = false)
    private Long sizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id", insertable = false, updatable = false)
    private Size size;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "in_stock")
    private Boolean inStock = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getSizeId() { return sizeId; }
    public void setSizeId(Long sizeId) { this.sizeId = sizeId; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getInStock() { return inStock; }
    public void setInStock(Boolean inStock) { this.inStock = inStock; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
