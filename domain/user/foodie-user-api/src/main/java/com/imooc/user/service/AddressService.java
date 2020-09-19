package com.imooc.user.service;


import com.imooc.user.pojo.UserAddress;
import com.imooc.user.pojo.bo.AddressBO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: 地址接口API
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@RequestMapping("address-api")
public interface AddressService {

    /**
     * 根据用户ID查询所有收货地址
     * @param userId
     * @return
     */
    @GetMapping("addressList")
    List<UserAddress> queryAll(@RequestParam("userId") String userId);

    /**
     * 用户新增地址
     * @param addressBO
     */
    @PostMapping("address")
    void addNewAdress(@RequestBody  AddressBO addressBO);

    /**
     * 用户修改地址
     * @param addressBO
     */
    @PutMapping("address")
    void updateUserAddress(@RequestBody AddressBO addressBO);

    /**
     * 删除地址
     * @param userId
     * @param addressId
     */
    @DeleteMapping("address")
    void delUserAddress(@RequestParam("userId") String userId,
                        @RequestParam("addressId") String addressId);

    /**
     * 设置默认地址
     * @param userId
     * @param addressId
     */
    @PostMapping("setDefaultAdress")
    void updateUserAddressToBeDefault(@RequestParam("userId") String userId,
                                      @RequestParam("addressId") String addressId);

    /**
     *  根据userID，addressId 查询用户地址
     * @param userId
     * @param addressId
     * @return
     */
    @GetMapping("queryAdress")
    UserAddress queryUserAdress(@RequestParam("userId") String userId,
                                @RequestParam(value = "addressId", required = false) String addressId);
}
