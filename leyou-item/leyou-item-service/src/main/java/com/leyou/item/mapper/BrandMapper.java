package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long> {

    @Insert("INSERT INTO TB_CATEGORY_BRAND (CATEGORY_ID, BRAND_ID) VALUES (#{cid}, #{bid})")
    public void saveCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT B.* FROM TB_BRAND B LEFT JOIN TB_CATEGORY_BRAND CB ON B.ID = CB.BRAND_ID WHERE CB.CATEGORY_ID = #{cid}")
    public List<Brand> queryBrandsByCid3(Long cid);

}
