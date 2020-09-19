package com.imooc.user.pojo.vo;

import lombok.Data;

/**
 * @program: foodie-dev
 * @description: usersVo
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class UsersVO {

    /**
     * 主键id 用户id
     */
    private String id;

    /**
     * 用户名 用户名
     */
    private String username;

    /**
     * 昵称 昵称
     */
    private String nickname;

    /**
     * 头像 头像
     */
    private String face;

    /**
     * 性别 性别 1:男  0:女  2:保密
     */
    private Integer sex;

    /**
     * 用户会话Token
     */
    private String userUniqueToken;

}
