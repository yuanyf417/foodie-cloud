package com.imooc.item.controller;

import com.imooc.controller.BaseController;
import com.imooc.item.pojo.Items;
import com.imooc.item.pojo.ItemsImg;
import com.imooc.item.pojo.ItemsParam;
import com.imooc.item.pojo.ItemsSpec;
import com.imooc.item.pojo.vo.CommentLevelCountsVO;
import com.imooc.item.pojo.vo.ItemInfoVO;
import com.imooc.item.service.ItemService;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: TODO
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@RestController
@Api(value = "商品接口" ,tags = {"商品信息展示相关接口"})
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;


    @ApiOperation( value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult carousel(
            @ApiParam(name = "itemId", value = "商品ID", required = true)
            @PathVariable String itemId) {
        if(StringUtils.isBlank( itemId )){
            return IMOOCJSONResult.errorMsg( null );
        }
        Items items = itemService.queryItemById( itemId );
        List<ItemsImg> itemsImgs = itemService.queryItemImgList( itemId );
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList( itemId );
        ItemsParam itemsParam = itemService.queryItemParam( itemId );
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem( items );
        itemInfoVO.setItemImgList( itemsImgs );
        itemInfoVO.setItemSpecList( itemsSpecs );
        itemInfoVO.setItemParams( itemsParam );

        return IMOOCJSONResult.ok( itemInfoVO );
    }


    @ApiOperation( value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品ID", required = true)
            @RequestParam String itemId) {
        if(StringUtils.isBlank( itemId )){
            return IMOOCJSONResult.errorMsg( null );
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts( itemId );

        return IMOOCJSONResult.ok( countsVO );
    }

    @ApiOperation( value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(name = "itemId", value = "商品ID", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评论等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "当前页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "页大小", required = false)
            @RequestParam Integer pageSize) {
        if(StringUtils.isBlank( itemId )){
            return IMOOCJSONResult.errorMsg( null );
        }

        if (page == null) {
            page = PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.queryPagedComments( itemId, level, page, pageSize );
        return IMOOCJSONResult.ok( pagedGridResult );
    }


    /*@ApiOperation( value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(
            @ApiParam(name = "keywords", value = "搜索关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "页大小", required = false)
            @RequestParam Integer pageSize) {
        if(StringUtils.isBlank( keywords )){
            return IMOOCJSONResult.errorMsg( null );
        }

        if (page == null) {
            page = PAGE;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.searchItems( keywords, sort, page, pageSize );
        return IMOOCJSONResult.ok( pagedGridResult );
    }

    @ApiOperation( value = "根据分类ID搜索商品列表", notes = "根据分类ID搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(name = "catId", value = "分类ID", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "页大小", required = false)
            @RequestParam Integer pageSize) {
        if(catId==null){
            return IMOOCJSONResult.errorMsg( null );
        }

        if (page == null) {
            page = PAGE;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.searchItemsByThirdCat( catId, sort, page, pageSize );
        return IMOOCJSONResult.ok( pagedGridResult );
    }*/

    /**
     * 刷新购物车商品信息
     *
     * @return
     */
    /*@GetMapping("/refresh")
    @ApiOperation(value = "刷新购物车商品信息", notes = "刷新购物车商品信息", httpMethod = "GET")
    public IMOOCJSONResult refresh(@RequestParam(value = "itemSpecIds") String specIds) {
        if (StringUtils.isBlank(specIds)) {
            return IMOOCJSONResult.errorMsg("没有商品信息");
        }
        List<ShopcartVO> list = itemService.queryItemsBySpecId(specIds);
        return IMOOCJSONResult.ok(list);
    }*/
}







