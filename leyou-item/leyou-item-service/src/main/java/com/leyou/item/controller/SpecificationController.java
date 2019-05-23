package com.leyou.item.controller;

import com.leyou.item.api.inner.ISpecificationService;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
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
@RequestMapping("spec")
public class SpecificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationController.class);

    @Autowired
    private ISpecificationService specificationService;

    /**
     * 根据商品分类id 查询规格参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsByCid(@PathVariable("cid") Long cid){

        LOGGER.info("request-log:-----> (GET) spec/groups/" + cid);

        if (cid == null || cid < 0)
            return ResponseEntity.badRequest().build();

        List<SpecGroup> groups = specificationService.querySpecGroupsByCid(cid);

        if (CollectionUtils.isEmpty(groups))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(groups);

    }

    /**
     * 根据商品分类id 查询规格参数组及组对应的规格参数
     * @param cid
     * @return
     */
    @GetMapping("groups/param/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsAndParamsByCid(@PathVariable("cid") Long cid){

        LOGGER.info("request-log:-----> (GET) spec/groups/param/" + cid);

        if (cid == null || cid < 0)
            return ResponseEntity.badRequest().build();

        List<SpecGroup> groups = specificationService.querySpecGroupsAndParamsByCid(cid);

        if (CollectionUtils.isEmpty(groups))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(groups);

    }

    /**
     * 新增规格参数组
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveGroup(@RequestBody SpecGroup specGroup){

        LOGGER.info("request-log:-----> (POST) spec/group  " + specGroup.toString());

        specificationService.saveSpecGroup(specGroup);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格参数组
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){

        LOGGER.info("request-log:-----> (PUT) spec/group  " + specGroup.toString());

        specificationService.updateSpecGroup(specGroup);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * 根据gid 删除规则参数组
     * @param gid
     */
    @DeleteMapping("group/{gid}")
    public ResponseEntity<Void> deleteGroupByGid(@PathVariable("gid") Long gid){

        LOGGER.info("request-log:-----> (DELETE) spec/group/" + gid);

        specificationService.deleteSpecGroupByGid(gid);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * 根据条件查询规格参数信息
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParamsByCdi(
                                            @RequestParam(value = "gid", required = false) Long gid,
                                            @RequestParam(value = "cid", required = false) Long cid,
                                            @RequestParam(value = "searching", required = false) Boolean searching,
                                            @RequestParam(value = "generic", required = false) Boolean generic){

        LOGGER.info("request-log:-----> (GET) spec/params?gid=" + gid + "&cid=" + cid + "&searching=" + searching + "&generic=" + generic);


        List<SpecParam> params = specificationService.querySpecParams(gid, cid, searching, generic);

        if (CollectionUtils.isEmpty(params))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(params);
    }

    /**
     * 新增规格参数
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveParams(@RequestBody SpecParam specParam){

        LOGGER.info("request-log:-----> (POST) spec/param  " + specParam.toString());

        specificationService.saveSpecParam(specParam);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 修改规则参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParams(@RequestBody SpecParam specParam){

        LOGGER.info("request-log:-----> (PUT) spec/param  " + specParam.toString());

        specificationService.updateParam(specParam);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deletePramsByPid(@PathVariable("pid") Long pid){

        LOGGER.info("request-log:-----> (DELETE) spec/param/" + pid);

        specificationService.deletePramsByPid(pid);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
