package com.leyou.item.api.outer;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据商品分类id 查询规格参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public List<SpecGroup> querySpecGroupsByCid(@PathVariable("cid") Long cid);



    /**
     * 根据条件查询规格参数信息
     * @param gid
     * @return
     */
    @GetMapping("params")
    public List<SpecParam> querySpecParamsByCdi(
                                            @RequestParam(value = "gid", required = false) Long gid,
                                            @RequestParam(value = "cid", required = false) Long cid,
                                            @RequestParam(value = "searching", required = false) Boolean searching,
                                            @RequestParam(value = "generic", required = false) Boolean generic);

    /**
     * 根据商品分类id 查询规格参数组及组对应的规格参数
     * @param cid
     * @return
     */
    @GetMapping("groups/param/{cid}")
    public List<SpecGroup> querySpecGroupsAndParamsByCid(@PathVariable("cid") Long cid);

}
