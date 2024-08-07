package neko.transaction.thirdparty.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.thirdparty.service.OSSService;
import neko.transaction.thirdparty.vo.OSSCallbackVo;
import neko.transaction.thirdparty.vo.OSSVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("oss")
public class OOSController {
    @Resource
    private OSSService ossService;

    /**
     * 获取oss上传信息
     */
    @SaCheckLogin
    @GetMapping("policy")
    public ResultObject<OSSVo> policy() {
        return ResultObject.ok(ossService.getPolicy());
    }

    /**
     * oss回调处理
     */
    @PostMapping("callback")
    public ResultObject<Object> callback(@Validated @RequestBody OSSCallbackVo vo){
        ossService.handleCallback(vo);

        return ResultObject.ok();
    }

    /**
     * oss图片上传，建议只提供给微服务远程调用
     */
    @PostMapping("upload_image")
    public ResultObject<String> uploadImage(@RequestPart MultipartFile file) throws IOException {
        return ResultObject.ok(ossService.uploadImage(file));
    }

    /**
     * oss图片批量上传，建议只提供给微服务远程调用
     */
    @PostMapping("upload_images")
    public ResultObject<List<String>> uploadImages(@RequestPart List<MultipartFile> files) throws IOException {
        return ResultObject.ok(ossService.uploadImages(files));
    }

    /**
     * 删除oss文件，建议只提供给微服务远程调用
     */
    @DeleteMapping("delete_file")
    public ResultObject<Object> deleteFile(@RequestParam String ossFilePath){
        ossService.deleteFile(ossFilePath);

        return ResultObject.ok();
    }

    /**
     * oss视频上传，建议只提供给微服务远程调用
     */
    @PostMapping("upload_video")
    public ResultObject<String> uploadVideo(@RequestPart MultipartFile file) throws IOException {
        return ResultObject.ok(ossService.uploadVideo(file));
    }

    /**
     * 批量删除oss文件，建议只提供给微服务远程调用
     */
    @DeleteMapping("delete_file_batch")
    public ResultObject<Object> deleteFileBatch(@RequestBody List<String> ossFilePaths){
        ossService.deleteFileBatch(ossFilePaths);

        return ResultObject.ok();
    }
}
