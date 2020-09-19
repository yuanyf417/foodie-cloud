package com.imooc.user.pojo.bo;

import lombok.Data;

/**
 * @program: foodie-dev
 * @description: 收货地址BO（新增、修改地址）
 * @author: YYF
 * @create: 2020-05-06 18:05
 **/

@Data
public class AddressBO {

    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String addressId;
}
