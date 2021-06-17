package com.imooc.user.service.center;


import com.imooc.user.pojo.Users;
import com.imooc.user.pojo.bo.center.CenterUserBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 *  foodie-dev
 *  用户中心Service
 * @author: YYF
 * @create: 2019-12-21 13:12
 **/
@FeignClient("foodie-user-service")
@RequestMapping("center-user-api")
public interface CenterUserService {

    /**
     * 根据userID查询用户名
     * @param userId 用户名
     * @return 是否存在
     */
    @GetMapping("profile")
    Users queryUserinfo(@RequestParam("userId") String userId);

    /**
     * 根据userId修改用户信息
     * @param userId
     * @param centerUserBO
     */
    @PutMapping("profile/{userId}")
    Users updateUserInfo(@PathVariable("userId") String userId, @RequestBody CenterUserBO centerUserBO);

    /**
     * 更新用户头像
     * @param userId
     * @param faceUrl
     * @return
     */
    @PostMapping("updatePhoto")
    Users updateUserFace(@RequestParam("userId") String userId,
                         @RequestParam("faceUrl") String faceUrl);



}
