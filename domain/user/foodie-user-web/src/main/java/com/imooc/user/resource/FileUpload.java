package com.imooc.user.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *  foodie-dev
 *  文件上传
 * @author: YYF
 * @create: 2020-05-09 11:53
 **/
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource( "classpath:file-upload-prod.properties" )
public class FileUpload {

    private String imageUserFaceLocation ;
    private String imageServerUrl;

    public String getImageUserFaceLocation() {
        return imageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        this.imageUserFaceLocation = imageUserFaceLocation;
    }

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }
}
