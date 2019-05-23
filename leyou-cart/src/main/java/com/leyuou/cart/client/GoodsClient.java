package com.leyuou.cart.client;

import com.leyou.item.api.outer.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
