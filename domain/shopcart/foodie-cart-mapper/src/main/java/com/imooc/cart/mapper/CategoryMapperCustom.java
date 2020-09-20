package com.imooc.cart.mapper;

import com.imooc.cart.pojo.vo.CategoryVO;
import com.imooc.cart.pojo.vo.NewitemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<NewitemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}