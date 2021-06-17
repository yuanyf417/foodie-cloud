package com.imooc.user.service.impl;

import com.imooc.user.pojo.bo.AddressBO;
import com.imooc.enums.YesOrNo;
import com.imooc.user.mapper.UserAddressMapper;
import com.imooc.user.pojo.UserAddress;
import com.imooc.user.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 *  foodie-dev
 *  TODO
 * @author: YYF
 * @create: 2020-05-06 22:08
 **/
@RestController
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        UserAddress ua = new UserAddress();
        ua.setUserId( userId );

        return userAddressMapper.select( ua );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewAdress(AddressBO addressBO) {
        //1. 判断当前是否存在地址 ，没有则新增为默认地址
        Integer isDefault = 0;
        List<UserAddress> userAddressList = this.queryAll( addressBO.getUserId() );
        if(CollectionUtils.isEmpty( userAddressList )) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();

        //2. 保存地址到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties( addressBO, newAddress );

        newAddress.setId( addressId );
        newAddress.setIsDefault( isDefault );
        newAddress.setCreatedTime( new Date(  ) );
        newAddress.setUpdatedTime( new Date(  ) );
        userAddressMapper.insert( newAddress );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {

        String addressId = addressBO.getAddressId();

        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties( addressBO, pendingAddress );

        pendingAddress.setId( addressId );
        pendingAddress.setUpdatedTime( new Date(  ) );

        userAddressMapper.updateByPrimaryKeySelective( pendingAddress );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delUserAddress(String userId, String addressId) {
        UserAddress delAddress = new UserAddress();

        delAddress.setUserId( userId );
        delAddress.setId( addressId );

        userAddressMapper.delete( delAddress );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {

        // 1. 查找默认地址， 设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId( userId );
        queryAddress.setIsDefault( YesOrNo.YES.type );
        List<UserAddress> select = userAddressMapper.select( queryAddress );

        select.stream().forEach( adress-> {
            adress.setIsDefault( YesOrNo.NO.type );
            userAddressMapper.updateByPrimaryKeySelective( adress );
        });

        // 2. 根据地址ID，设置默认地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId( addressId );
        defaultAddress.setUserId( userId );
        defaultAddress.setIsDefault( YesOrNo.YES.type );
        userAddressMapper.updateByPrimaryKeySelective( defaultAddress );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAdress(String userId, String addressId) {

        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId( userId );
        queryAddress.setId( addressId );

        return userAddressMapper.selectOne( queryAddress );
    }
}
