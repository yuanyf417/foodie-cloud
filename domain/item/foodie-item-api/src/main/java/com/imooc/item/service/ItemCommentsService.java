package com.imooc.item.service;

import com.imooc.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: foodie-cloud
 * @description: TODO
 * @author: YYF
 * @create: 2020-09-19 19:34
 **/
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
