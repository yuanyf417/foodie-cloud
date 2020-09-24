package com.imooc.item.service;


import com.imooc.item.pojo.Items;
import com.imooc.item.pojo.ItemsImg;
import com.imooc.item.pojo.ItemsParam;
import com.imooc.item.pojo.ItemsSpec;
import com.imooc.item.pojo.vo.CommentLevelCountsVO;
import com.imooc.item.pojo.vo.ShopcartVO;
import com.imooc.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: 分类
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@FeignClient("foodie-item-service")
@RequestMapping("item-api")
public interface ItemService {

    /**
     * 根据商品ID 查询商品信息
     * @param itemId
     * @return
     */
    @GetMapping("item")
    Items queryItemById(@RequestParam("itemId") String itemId);
    /**
     * 根据商品ID 查询商品图片列表
     * @param itemId
     * @return
     */
    @GetMapping("itemImages")
    List<ItemsImg> queryItemImgList(@RequestParam("itemId") String itemId);
    /**
     * 根据商品ID 查询商品规格信息
     * @param itemId
     * @return
     */
    @GetMapping("itemSpecs")
    List<ItemsSpec> queryItemSpecList(@RequestParam("itemId") String itemId);
    /**
     * 根据商品ID 查询商品参数信息
     * @param itemId
     * @return
     */
    @GetMapping("itemParam")
    ItemsParam queryItemParam(@RequestParam("itemId") String itemId);

    /**
     * 根据商品ID 查询商品评价数量信息
     * @param itemId
     * @return
     */
    @GetMapping("countComments")
    CommentLevelCountsVO queryCommentCounts(@RequestParam("itemId") String itemId);

    /**
     * 根据商品ID和商品评价等级查询商品评价内容
     * @param itemId
     * @param level
     * @return
     */
    @GetMapping("pagedComments")
    PagedGridResult queryPagedComments(@RequestParam("itemId") String itemId,
                                       @RequestParam(value = "level", required = false) Integer level,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize);
//
//    /**
//     * 搜索商品列表
//     * @param keywords
//     * @param sort
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

//    /**
//     * 根据商品分类搜索商品列表
//     * @param catId
//     * @param sort
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("")
//    PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 通过规格ID查询最新商品信息
     * @param specIds 规格ID
     * @return
     */
    @GetMapping("getCartBySpecIds")
    List<ShopcartVO> queryItemsBySpecIds(@RequestParam("specIds") String specIds);

    /**
     * 根据商品规格id获取规格对象的具体信息
     * @param specId
     * @return
     */
    @GetMapping("singleItemSpec")
    ItemsSpec queryItemSpecById(@RequestParam("specId") String specId);

    /**
     * 根据商品id获取商品主图
     * @param itemId
     * @return
     */
    @GetMapping("primaryImage")
    String queryItemMainImgById(@RequestParam("itemId") String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    @PostMapping("decreaseStock")
    void decreaseItemSpecStock(@RequestParam("specId") String specId, @RequestParam("buyCounts")  int buyCounts);
}
