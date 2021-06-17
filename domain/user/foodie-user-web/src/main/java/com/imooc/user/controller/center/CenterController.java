package com.imooc.user.controller.center;

import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.user.pojo.Users;
import com.imooc.user.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  foodie-dev
 *  用户中心
 * @author: YYF
 * @create: 2020-05-09 01:55
 **/
@Api(value = "center - 用户中心", tags = {"用户中心的相关接口"})
@Slf4j
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation( value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        Users users = centerUserService.queryUserinfo( userId );
        return IMOOCJSONResult.ok(users);
    }
}
