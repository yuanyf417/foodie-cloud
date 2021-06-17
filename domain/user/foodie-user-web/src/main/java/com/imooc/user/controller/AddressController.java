package com.imooc.user.controller;

import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.user.pojo.UserAddress;
import com.imooc.user.pojo.bo.AddressBO;
import com.imooc.user.service.AddressService;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  foodie-dev
 *  地址相关接口
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@Api(value = "地址相关接口", tags = {"地址相关的接口"})
@RequestMapping("address")
@RestController
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5.设置默认地址
     */
    @Autowired
    private AddressService addressService;

    @ApiOperation( value = "根据用户ID查询收货地址", notes = "根据用户ID查询收货地址", httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {
        if (StringUtils.isBlank( userId )) {
            return IMOOCJSONResult.errorMsg( "" );
        }
        List<UserAddress> userAddresses = addressService.queryAll( userId );
        return IMOOCJSONResult.ok( userAddresses );
    }

    @ApiOperation( value = "新增收货地址", notes = "新增收货地址", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @ApiParam(name = "adressBO", value = "地址对象", required = true)
            @RequestBody AddressBO addressBO) {
        if (addressBO == null) {
            return IMOOCJSONResult.errorMsg( "" );
        }

        IMOOCJSONResult IMOOCJSONResult = checkAddress( addressBO );
        if(IMOOCJSONResult.getStatus() != 200) {
            return IMOOCJSONResult;
        }

        addressService.addNewAdress( addressBO );
        return IMOOCJSONResult.ok( );
    }

    @ApiOperation( value = "修改地址", notes = "修改收货地址", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @ApiParam(name = "adressBO", value = "地址对象", required = true)
            @RequestBody AddressBO addressBO) {
        if (addressBO == null) {
            return IMOOCJSONResult.errorMsg( "" );
        }
        if(StringUtils.isBlank( addressBO.getAddressId() )) {
            return IMOOCJSONResult.errorMsg( "修改地址错误：AddressId 不能为空" );
        }

        IMOOCJSONResult IMOOCJSONResult = checkAddress( addressBO );
        if(IMOOCJSONResult.getStatus() != 200) {
            return IMOOCJSONResult;
        }

        addressService.updateUserAddress( addressBO );
        return IMOOCJSONResult.ok( );
    }

    @ApiOperation( value = "删除地址", notes = "删除收货地址", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址ID", required = true)
            @RequestParam String addressId) {

        if(StringUtils.isBlank( addressId ) || StringUtils.isBlank( addressId )) {
            return IMOOCJSONResult.errorMsg( "" );
        }

        addressService.delUserAddress( userId, addressId );
        return IMOOCJSONResult.ok( );
    }

    @ApiOperation( value = "设置默认地址", notes = "设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址ID", required = true)
            @RequestParam String addressId) {

        if(StringUtils.isBlank( addressId ) || StringUtils.isBlank( addressId )) {
            return IMOOCJSONResult.errorMsg( "" );
        }

        addressService.updateUserAddressToBeDefault( userId, addressId );
        return IMOOCJSONResult.ok( );
    }

    private IMOOCJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }

}
