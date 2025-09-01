package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts", schema = "store_api")
public class Cart {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator // optional, lets Hibernate generate UUID automatically
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ColumnDefault("(curdate())")

    @Column(name = "date_created",insertable = false,updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE)
    private Set<CartItem> cartItems = new LinkedHashSet<>();
    public BigDecimal getTotalPrice()
    {
        return cartItems.stream().map(CartItem ::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
    };
}