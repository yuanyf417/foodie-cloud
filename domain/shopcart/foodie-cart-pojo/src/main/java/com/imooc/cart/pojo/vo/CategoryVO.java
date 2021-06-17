package com.imooc.cart.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 *  foodie-dev
 *  二级分类VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private String fatherId;
    private List<SubCategoryVO> subCatList;

}
