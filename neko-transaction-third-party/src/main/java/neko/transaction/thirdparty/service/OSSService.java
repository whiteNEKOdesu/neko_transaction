package neko.transaction.thirdparty.service;

import neko.transaction.thirdparty.vo.OSSCallbackVo;
import neko.transaction.thirdparty.vo.OSSVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OSSService {
    OSSVo getPolicy();

    void handleCallback(OSSCallbackVo vo);

    String uploadImage(MultipartFile file) throws IOException;

    List<String> uploadImages(List<MultipartFile> files) throws IOException;

    void deleteFile(String ossFilePath);

    String uploadVideo(MultipartFile file) throws IOException;

    void deleteFileBatch(List<String> ossFilePaths);
}
