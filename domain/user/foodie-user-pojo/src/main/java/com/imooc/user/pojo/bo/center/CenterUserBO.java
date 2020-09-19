package com.imooc.user.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @author yyf
 * @date 2019-11-06 12:14
 */
@Data
@ApiModel(value = "修改用户信息BO", description = "用户中心中的保存修改用户信息")
public class CenterUserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "imooc",required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123456",required = true)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "和密码一致", required = true)
    private String confirmPassword;

    @ApiModelProperty(value = "用户昵称", name = "nickname", example = "慕课", required = true)
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

    @ApiModelProperty(value = "真实姓名", name = "realname", example = "小木木", required = false)
    @Length(max = 12, message = "用户昵称不能超过12位")
    private String realname;

    @ApiModelProperty(value = "手机号", name = "mobile", example = "13333333333", required = false)
    @Pattern( regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    private String mobile;

    @ApiModelProperty(value = "邮箱", name = "email", example = "yuanyf417@163.com", required = false)
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "性别", name = "sex", example = "0:女 1：男 2：保密", required = false)
    @Min( value = 0, message = "性别选择不正确")
    @Max( value = 2, message = "性别选择不正确")
    private Integer sex;

    @ApiModelProperty(value = "生日", name = "birthday", example = "1900-01-01", required = false)
    private String birthday;



}
