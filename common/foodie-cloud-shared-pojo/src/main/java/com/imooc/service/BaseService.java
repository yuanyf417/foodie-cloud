package com.imooc.service;

import com.github.pagehelper.PageInfo;
import com.imooc.pojo.PagedGridResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: Service基类
 * @author: YYF
 * @create: 2020-05-09 18:28
 **/
@Service
public class BaseService {

    /**
     * 分页组装
     * @param list
     * @param page
     * @return
     */
    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>( list );
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage( page );
        pagedGridResult.setRows( list );
        pagedGridResult.setTotal( pageInfo.getPages() );
        pagedGridResult.setRecords( pageInfo.getTotal() );
        return pagedGridResult;
    }

}
