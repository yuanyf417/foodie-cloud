package com.imooc.user.service.impl.center;

import com.imooc.user.mapper.UsersMapper;
import com.imooc.user.pojo.Users;
import com.imooc.user.pojo.bo.center.CenterUserBO;
import com.imooc.user.service.center.CenterUserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: foodie-dev
 * @description: TODO
 * @author: YYF
 * @create: 2020-05-09 01:59
 **/
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

//    @Autowired
//    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserinfo(String userId) {

        Users users = usersMapper.selectByPrimaryKey( userId );
        users.setPassword( null );
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {

        Users updateUser = new Users();
        BeanUtils.copyProperties( centerUserBO, updateUser );
        updateUser.setId( userId );
        updateUser.setUpdatedTime( new Date(  ) );
        usersMapper.updateByPrimaryKeySelective( updateUser );

        return queryUserinfo( userId );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {

        Users updateUser = new Users();
        updateUser.setId( userId );
        updateUser.setFace( faceUrl );
        updateUser.setUpdatedTime( new Date(  ) );

        usersMapper.updateByPrimaryKeySelective( updateUser );

        return queryUserinfo( userId );
    }


}
