package com.imooc.user.service;


import com.imooc.user.pojo.Users;
import com.imooc.user.pojo.bo.UserBO;
import org.springframework.web.bind.annotation.*;

/**
 * @program: foodie-dev
 * @description: 用户Service
 * @author: YYF
 * @create: 2019-12-21 13:12
 **/
@RequestMapping("user-api")
public interface UserService {

    /**
     * 查询用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    @GetMapping("user/exists")
    boolean queryUsernameIsExist(@RequestParam("userName") String username);

    /**
     * 创建一个用户
     * @param userBO 接收一个bo对象
     * @return users 返回一个users类型
     */
    @PostMapping("user")
    Users createUser(@RequestBody UserBO userBO);

    /**
     * 检索用户名和密码是否匹配
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @GetMapping("verify")
    Users queryUSerForLogin(@RequestParam("userName") String username,
                            @RequestParam("password") String password);
}
