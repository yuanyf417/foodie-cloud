package com.imooc.item.pojo.vo;



import com.imooc.item.pojo.Items;
import com.imooc.item.pojo.ItemsImg;
import com.imooc.item.pojo.ItemsParam;
import com.imooc.item.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 *  foodie-dev
 *  商品详情VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams ;

}
