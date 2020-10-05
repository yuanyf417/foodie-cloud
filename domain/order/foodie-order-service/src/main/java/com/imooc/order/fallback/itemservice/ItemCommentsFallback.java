package com.imooc.order.fallback.itemservice;

import com.google.common.collect.Lists;
import com.imooc.item.pojo.vo.MyCommentVO;
import com.imooc.pojo.PagedGridResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @program: foodie-cloud
 * @description: ItemCommentsFeignClient的降级类
 * @author: YYF
 * @create: 2020-10-06 03:19
 **/
@Component
@RequestMapping("JokeJoke")
public class ItemCommentsFallback implements ItemCommentsFeignClient {

    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        MyCommentVO commentVO = new MyCommentVO();
        commentVO.setContent( "正在加载中..." );
        PagedGridResult result = new PagedGridResult();
        result.setRows( Lists.newArrayList( commentVO ) );
        result.setTotal( 1 );
        result.setRecords( 1 );
        return result;
    }

    @Override
    public void saveComments(Map<String, Object> map) {

    }
}
