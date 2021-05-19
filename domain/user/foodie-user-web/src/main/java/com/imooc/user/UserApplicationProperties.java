package com.imooc.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author 秋风无赖.
 * date 2021/5/19 01:16.
 */
@Configuration
@RefreshScope
@Data
public class UserApplicationProperties {

    @Value("${userservice.registration.disabled}")
    private String  disableRegistration;
}
