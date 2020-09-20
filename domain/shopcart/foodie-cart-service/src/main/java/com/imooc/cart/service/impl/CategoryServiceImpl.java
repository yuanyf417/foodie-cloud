package com.imooc.cart.service.impl;

import com.imooc.cart.mapper.CategoryMapper;
import com.imooc.cart.mapper.CategoryMapperCustom;
import com.imooc.cart.pojo.Category;
import com.imooc.cart.pojo.vo.CategoryVO;
import com.imooc.cart.pojo.vo.NewitemsVO;
import com.imooc.cart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: foodie-dev
 * @description: TODO
 * @author: YYF
 * @create: 2020-05-01 00:08
 **/
@RestController
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {

        Example example = new Example( Category.class );
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo( "type" ,1 );

        List<Category> carousels = categoryMapper.selectByExample( example );
        return carousels;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {

        return categoryMapperCustom.getSubCatList( rootCatId );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewitemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String,Object> map = new HashMap<String,Object>(  );
        map.put( "rootCatId", rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy( map );
    }
}
