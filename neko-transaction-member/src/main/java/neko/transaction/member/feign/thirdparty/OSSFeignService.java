package neko.transaction.member.feign.thirdparty;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 第三方服务远程调用
 */
@FeignClient(value = ServiceName.THIRD_PARTY_SERVICE, contextId = "OSS")
public interface OSSFeignService {
    /**
     * oss图片上传
     * @param file 图片
     * @return 图片url
     */
    @PostMapping(value = "oss/upload_image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultObject<String> uploadImage(@RequestPart MultipartFile file);

    /**
     * 删除oss文件
     * @param ossFilePath 文件url
     * @return 响应结果
     */
    @DeleteMapping("oss/delete_file")
    ResultObject<Object> deleteFile(@RequestParam String ossFilePath);

    /**
     * oss视频上传
     * @param file 视频文件
     * @return 视频url
     */
    @PostMapping(value = "oss/upload_video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultObject<String> uploadVideo(@RequestPart MultipartFile file);

    /**
     * 批量删除oss文件
     * @param ossFilePaths 要删除的文件url
     * @return 响应结果
     */
    @DeleteMapping("oss/delete_file_batch")
    ResultObject<Object> deleteFileBatch(@RequestBody List<String> ossFilePaths);
}
