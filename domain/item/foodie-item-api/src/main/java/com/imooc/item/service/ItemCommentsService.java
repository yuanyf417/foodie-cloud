package com.imooc.item.service;

import com.imooc.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 内部的降级（商品中心） 放到item-service里实现
 *
 * 调用方的降级（订单中心，调用商品中心服务） 由订单中心定义的降级逻辑
 *
 * @program: foodie-cloud
 * @description: TODO
 * @author: YYF
 * @create: 2020-09-19 19:34
 **/
@FeignClient("foodie-item-service")
@RequestMapping("item-comments-api")
public interface ItemCommentsService {

    /**
     * 查询我的评论
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("myComments")
    PagedGridResult queryMyComments(@RequestParam("userId") String userId,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @PostMapping("saveComments")
    public void saveComments(@RequestBody Map<String ,Object> map);

}
