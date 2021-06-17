package com.imooc.user.exception;

import com.imooc.pojo.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 *  foodie-dev
 *  TODO
 * @author: YYF
 * @create: 2020-05-09 13:07
 **/
@RestControllerAdvice
public class CustomExceptionHandler {

    //MaxUploadSizeExceededException

    /**
     * 上传文件超过500K，捕获异常
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return IMOOCJSONResult.errorMsg( "文件上传大小不能超过500K，请压缩或降低图片质量后上传！" );
    }
}
