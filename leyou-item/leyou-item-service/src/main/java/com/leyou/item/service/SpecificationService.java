package com.leyou.item.service;

import com.leyou.item.api.inner.ISpecificationService;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService implements ISpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    /**
     * 根据商品分类id 查询规格参数组
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecGroupsByCid(Long cid) {

        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);

        return groupMapper.select(specGroup);
    }

    /**
     * 根据条件查询规格参数信息
     * @param gid
     * @return
     */
    @Override
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {

        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);

        return paramMapper.select(specParam);
    }

    /**
     * 新增规则参数组
     * @param specGroup
     */
    @Override
    public void saveSpecGroup(SpecGroup specGroup) {

        groupMapper.insertSelective(specGroup);
    }

    /**
     * 修改规格参数组
     * @param specGroup
     */
    @Override
    public void updateSpecGroup(SpecGroup specGroup) {

        groupMapper.updateByPrimaryKeySelective(specGroup);
    }

    /**
     * 根据gid 删除规则参数组
     * @param gid
     */
    @Override
    public void deleteSpecGroupByGid(Long gid) {

        groupMapper.deleteByPrimaryKey(gid);
    }

    /**
     * 新增规格参数
     * @return
     */
    @Override
    public void saveSpecParam(SpecParam specParam) {

        paramMapper.insertSelective(specParam);
    }

    /**
     * 修改规格参数
     * @param specParam
     */
    @Override
    public void updateParam(SpecParam specParam) {

        paramMapper.updateByPrimaryKeySelective(specParam);
    }

    /**
     * 根据pid 删除规格参数
     * @param pid
     */
    @Override
    public void deletePramsByPid(Long pid) {

        paramMapper.deleteByPrimaryKey(pid);
    }

    /**
     * 根据商品分类id 查询规格参数组及组对应的规格参数
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecGroupsAndParamsByCid(Long cid) {

        // 根据分类id 查询规格参数组
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);

        List<SpecGroup> specGroups = groupMapper.select(specGroup);

        // 查询每个组对应的规格参数并赋值
        specGroups.forEach(group -> {

            SpecParam specParam = new SpecParam();
            specParam.setGroupId(group.getId());

            List<SpecParam> specParams = paramMapper.select(specParam);
            group.setParams(specParams);
        });

        return specGroups;
    }
}
