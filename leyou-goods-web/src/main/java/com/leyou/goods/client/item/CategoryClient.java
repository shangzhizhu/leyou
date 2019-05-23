package com.leyou.goods.client.item;

import com.leyou.item.api.outer.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
