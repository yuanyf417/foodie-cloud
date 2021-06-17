package com.imooc.user.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.user.pojo.Users;
import com.imooc.user.pojo.bo.center.CenterUserBO;
import com.imooc.user.pojo.vo.UsersVO;
import com.imooc.user.resource.FileUpload;
import com.imooc.user.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *  foodie-dev
 *  用户信息
 * @author: YYF
 * @create: 2020-05-09 01:55
 **/
@Api(value = "用户信息相关接口", tags = {"用户信息相关接口"})
@Slf4j
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation( value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @RequestMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "centerUserBO", value = "用户BO", required = true)
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        // 判断是否包含错误信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors( result );
            return IMOOCJSONResult.errorMap( errors );
        }

        Users users = centerUserService.updateUserInfo( userId ,centerUserBO);
        // users = setNullProperty( users );

        // 生成用户token，存入redis会话
        UsersVO usersVO = conventUserVO( users );
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson( usersVO ), true );

        return IMOOCJSONResult.ok();
    }

    @ApiOperation( value = "修改用户头像", notes = "修改用户头像", httpMethod = "POST")
    @RequestMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像信息", required = true)
            @RequestBody MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {



        // 定义头像保存的地址
        //String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();

        // 用户独立保存
        String uploadPathPrefix = File.separator + userId;

        //开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                // 获得文件名称
                String filename = file.getOriginalFilename();

                if (StringUtils.isNotBlank( filename )) {
                    //face-{userId}.png
                    String[] fileNameArr = filename.split( "\\." );

                    //获取文件后缀
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    if (!suffix.equalsIgnoreCase( "png" ) &&
                            !suffix.equalsIgnoreCase( "jpg" ) &&
                            !suffix.equalsIgnoreCase( "jpeg" )
                    ) {
                        return IMOOCJSONResult.errorMsg( "图片格式不正确" );
                    }

                    // 重组文件名 覆盖式上传
                    String newFileName = "face-" + userId + "." + suffix;

                    // 上传头像保存位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                    // 用户提供web服务的地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File( finalFacePath );

                    if (outFile.getParent() != null) {
                        // 创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    // 文件输出的路径
                    fileOutputStream = new FileOutputStream( outFile );
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy( inputStream, fileOutputStream );
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream !=null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            return IMOOCJSONResult.errorMsg( "文件不能为空" );
        }

        String imageServerUrl = fileUpload.getImageServerUrl();


        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix;
        // 更新用户头像到数据库
        Users users = centerUserService.updateUserFace( userId, finalUserFaceUrl
        // 由于浏览器可能存在缓存，所以加上时间戳保证更新后他的图片可以刷新
        + "?t=" + DateUtil.getCurrentDateString( DateUtil.DATE_PATTERN ) );

        //users = setNullProperty( users );

        // 生成用户token，存入redis会话
        UsersVO usersVO = conventUserVO( users );

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson( usersVO ), true );


        return IMOOCJSONResult.ok();
    }

    /**
     * 未通过验证的错误信息
     * @param result
     * @return
     */
    private Map<String,String> getErrors(BindingResult result) {

        Map<String,String> map = new HashMap<>(  );
        List<FieldError> fieldErrors = result.getFieldErrors();
        fieldErrors.stream().forEach( fieldError -> {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            map.put( field,defaultMessage );
        } );

        return map;
    }

    /**
     * 清除隐私信息
     *
     * @param result userResult
     * @return Users
     */
    private Users setNullProperty(Users result) {
        result.setPassword(null);
        result.setMobile(null);
        result.setEmail(null);
        result.setCreatedTime(null);
        result.setUpdatedTime(null);
        result.setBirthday(null);

        return result;
    }

    /**
     * UsersVO 转换， 并保存至Redis
     *
     * @param result
     * @return
     */
    private UsersVO conventUserVO(Users result) {

        String uniqueToke = UUID.randomUUID().toString().trim();
        redisOperator.set( REDIS_USER_TOKEN + ":" + result.getId(), uniqueToke );

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties( result, usersVO );
        usersVO.setUserUniqueToken( uniqueToke );
        return usersVO;
    }
}
