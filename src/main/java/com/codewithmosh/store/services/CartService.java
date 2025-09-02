package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {
    public CartRepository cartRepository;
    public CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart()
    {
        var cart=new Cart();
        cartRepository.save(cart);
        var cartDto=cartMapper.toDto(cart);
        return cartDto;
    }
    public CartItemDto addToCart(UUID cartId, Long productId)
    {
        var cart=cartRepository.findById(cartId).orElse(null);

        if(cart==null)
        {
           throw  new CartNotFoundException();
        }

        var product=productRepository.findById(productId).orElse(null);
            if(product==null)
        {
            throw new ProductNotFoundException();
        }
        var cartItem=  cart.getCartItems().stream().filter(item->item.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
        if(cartItem!=null)
        {
            cartItem.setQuantity(cartItem.getQuantity()+1);

        }else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);

        }
        cartRepository.save(cart);
        var cartItemDto=cartMapper.toDto(cartItem);
        return cartItemDto;
    }
    public CartDto getCartDto(UUID cartId)
    {
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null)
        {
            throw new CartNotFoundException();
        }
        var cartDto=cartMapper.toDto(cart);
        return cartDto;
    }
    public void deleteItem(UUID cartId,Long productId)
    {
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null)
        {
                throw new CartNotFoundException();
        }
        var cartItem=  cart.getCartItems().stream().filter(item->item.getProduct().getId().equals(productId)).findFirst().orElse(null);
        if(cartItem==null)
        {
            throw new ProductNotFoundException();
        }
        cart.getCartItems().remove(cartItem);
        cartItem.setCart(null);
        cartRepository.save(cart);
    }
    public void clear(UUID cartId)
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.getCartItems().forEach(item -> item.setCart(null));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
