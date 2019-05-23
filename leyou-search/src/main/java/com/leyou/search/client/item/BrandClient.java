package com.leyou.search.client.item;

import com.leyou.item.api.outer.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
