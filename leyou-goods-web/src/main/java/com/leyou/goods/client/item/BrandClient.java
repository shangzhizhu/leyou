package com.leyou.goods.client.item;

import com.leyou.item.api.outer.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
