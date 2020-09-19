package com.imooc.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.imooc.enums.CommentLevel;
import com.imooc.enums.YesOrNo;
import com.imooc.item.mapper.*;
import com.imooc.item.pojo.*;
import com.imooc.item.pojo.vo.CommentLevelCountsVO;
import com.imooc.item.pojo.vo.ItemCommentVO;
import com.imooc.item.pojo.vo.ShopcartVO;
import com.imooc.item.service.ItemService;
import com.imooc.pojo.PagedGridResult;
import com.imooc.utils.DesensitizationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: foodie-dev
 * @description: TODO
 * @author: YYF
 * @create: 2020-05-06 12:47
 **/
@RestController
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey( itemId );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {

        Example itemsImgExp = new Example( ItemsImg.class );
        Example.Criteria criteria = itemsImgExp.createCriteria();
        criteria.andEqualTo( "itemId",itemId );

        return itemsImgMapper.selectByExample( itemsImgExp );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {

        Example itemsSpecExp = new Example( ItemsSpec.class );
        Example.Criteria criteria = itemsSpecExp.createCriteria();
        criteria.andEqualTo( "itemId",itemId );

        return itemsSpecMapper.selectByExample( itemsSpecExp );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {

        Example itemsParamExp = new Example( ItemsParam.class );
        Example.Criteria criteria = itemsParamExp.createCriteria();
        criteria.andEqualTo( "itemId",itemId );

        return itemsParamMapper.selectOneByExample( itemsParamExp );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {

        Integer goodCommentCounts = getCommentCounts( itemId, CommentLevel.GOOD.type );
        Integer normalCommentCounts = getCommentCounts( itemId, CommentLevel.NORMAL.type );
        Integer badCommentCounts = getCommentCounts( itemId, CommentLevel.BAD.type );
        Integer totalCommentCounts = goodCommentCounts + normalCommentCounts + badCommentCounts;

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts( totalCommentCounts );
        countsVO.setGoodCounts( goodCommentCounts );
        countsVO.setNormalCounts( normalCommentCounts );
        countsVO.setBadCounts( badCommentCounts );

        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level,Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>(  );
        map.put( "itemId",itemId );
        map.put( "level",level );

        // mybatis-pagehelper
        PageHelper.startPage( page, pageSize );
        List<ItemCommentVO> itemCommentVOS = itemsMapperCustom.queryItemComments( map );
        itemCommentVOS.stream().forEach( item-> item.setNickname( DesensitizationUtil.commonDisplay( item.getNickname() ) ));

        return setterPagedGrid(itemCommentVOS, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] split = specIds.split(",");
        return itemsMapperCustom.searchItemsBySpecId( Arrays.asList( split ) ); // Collections.singletonList(specIds)
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {

        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain( YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        // synchronized： 不推荐，集群下无用，性能低下
        // 锁数据库： 不推荐，导致数据库性能低下

        //1. 查询库存
        //2. 判断库存，是否能减少到0一下

        // lockUtil.getLock();--加锁

        //lockUtil.unLock();--解锁

        // 乐观锁
        int result = itemsMapperCustom.decreaseItemSpecStock(specId,buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足");
        }

    }

    private PagedGridResult setterPagedGrid(List<?> list,Integer page) {
        PagedGridResult pagedGridResult = new PagedGridResult();
        PageInfo<?> pageInfo = new PageInfo<>( list );
        pagedGridResult.setPage( page );
        pagedGridResult.setRows( list );
        pagedGridResult.setTotal( pageInfo.getPages() );
        pagedGridResult.setRecords( pageInfo.getTotal() );
        return pagedGridResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level) {

        ItemsComments condition = new ItemsComments();
        condition.setItemId( itemId );
        if (level != null) {
            condition.setCommentLevel( level );
        }

        return itemsCommentsMapper.selectCount( condition );
    }


}
