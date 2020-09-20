package com.imooc.cart.service;


import com.imooc.cart.pojo.Category;
import com.imooc.cart.pojo.vo.CategoryVO;
import com.imooc.cart.pojo.vo.NewitemsVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: 分类
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@RequestMapping("cart-category-api")
public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return
     */
    @GetMapping("list")
    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类ID查询子分类信息
     * @param rootCatId
     * @return
     */
    @GetMapping("subList")
    List<CategoryVO> getSubCatList(Integer rootCatId);


    /**
     * 查询首页每一个一级分类下的6条最新商品数据
     * @param rootCatId
     * @return
     */
    @GetMapping("sixNewLazy")
    List<NewitemsVO> getSixNewItemsLazy(Integer rootCatId);
}
