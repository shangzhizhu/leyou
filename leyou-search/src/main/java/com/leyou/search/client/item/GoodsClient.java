package com.leyou.search.client.item;

import com.leyou.item.api.outer.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
