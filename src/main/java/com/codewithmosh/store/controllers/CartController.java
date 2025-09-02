package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartRepository cartRepository;
    private  final CartMapper  cartMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder)
    {
        var cartDto=cartService.createCart();
        var uri=uriComponentsBuilder.path("/cart/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addProductToCart(@PathVariable UUID cartId,@RequestBody AddItemToCartRequest request)
    {
        var cartItemDto=cartService.addToCart(cartId,request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId)
    {
       var cartDto=cartService.getCartDto(cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }
    @DeleteMapping("/{cardId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable("cardId") UUID cartId,@PathVariable("productId") Long productId)
    {   cartService.deleteItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{cardId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cardId") UUID cartId) {
         cartService.clear(cartId);
        return ResponseEntity.noContent().build();
    }
    //EXCEPTION HANDLERS
    @ExceptionHandler(value = {CartNotFoundException.class})
    public ResponseEntity<Map<String,String>> handleCarNotFound()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Cart not found"));
    }
    @ExceptionHandler(value={ProductNotFoundException.class})
    public ResponseEntity<Map<String,String>> handleProductNotFound()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Product not found in the cart"));
    }

}
