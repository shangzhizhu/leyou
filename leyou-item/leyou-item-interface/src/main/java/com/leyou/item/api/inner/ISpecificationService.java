package com.leyou.item.api.inner;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

public interface ISpecificationService {

    /**
     * 根据商品分类id 查询规格参数组
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecGroupsByCid(Long cid);

    /**
     * 根据条件查询规格参数信息
     * @param gid
     * @return
     */
    List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic);

    /**
     * 新增规格参数组
     * @param specGroup
     */
    void saveSpecGroup(SpecGroup specGroup);

    /**
     * 修改规格参数组
     * @param specGroup
     */
    void updateSpecGroup(SpecGroup specGroup);

    /**
     * 根据gid 删除规则参数组
     * @param gid
     */
    void deleteSpecGroupByGid(Long gid);

    /**
     * 新增规格参数
     * @return
     */
    void saveSpecParam(SpecParam specParam);

    /**
     * 修改规格参数
     * @param specParam
     */
    void updateParam(SpecParam specParam);

    /**
     * 根据pid 删除规格参数
     * @param pid
     */
    void deletePramsByPid(Long pid);

    /**
     * 根据商品分类id 查询规格参数组及组对应的规格参数
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecGroupsAndParamsByCid(Long cid);
}
