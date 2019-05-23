package com.leyuou.cart.controller;

import com.leyuou.cart.pojo.Cart;
import com.leyuou.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    /**
     * 购物车添加商品
     * @param cart
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){

        LOGGER.info("request-log: (POST) " + cart.toString());

        this.cartService.addCart(cart);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){

        LOGGER.info("request-log: (GET) 查询购物车-无请求参数" );

        List<Cart> carts = this.cartService.queryCarts();

        if (CollectionUtils.isEmpty(carts))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestBody Cart cart){

        LOGGER.info("request-log: (PUT) " + cart.toString());

        this.cartService.updateCart(cart);

        return ResponseEntity.noContent().build();
    }

    /**
     * 删除购物车中的商品
     * @param skuId
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId){

        LOGGER.info("request-log: (DELETE) " + skuId);

        this.cartService.deleteCart(skuId);

        return ResponseEntity.ok().build();
    }
}
