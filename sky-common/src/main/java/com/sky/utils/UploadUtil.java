package com.sky.utils;

import com.sky.exception.FileUploadException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class UploadUtil {

    private String[] ALLOWED_EXTENSIONS;
    private String defaultUploadDir;
    private String defaultWebUrlPrefix;
    private Long maxFileSize;

    public String upload(MultipartFile file) throws IOException {
        // 参数校验
        validateFile(file);

        // 生成唯一文件名
        String filename = Objects.requireNonNull(file.getOriginalFilename());
        String extension = getFileExtension(filename);

        // 检查文件类型
        if (!isAllowedExtension(extension)) {
            throw new FileUploadException("不支持的文件类型: " + extension);
        }

        String newName = generateUniqueFileName(extension);

        // 确保目录存在
        Path uploadPath = Paths.get(defaultUploadDir);
        ensureDirectoryExists(uploadPath);

        // 构建完整路径
        Path filePath = uploadPath.resolve(newName);
        File dest = filePath.toFile();

        // 保存文件
        saveFile(file, dest);

        // 返回访问URL
        return defaultWebUrlPrefix + newName;
    }

    /**
     * 参数校验
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("请选择上传的文件");
        }

        if (file.getSize() > maxFileSize) {
            throw new FileUploadException("文件大小超过限制: " + maxFileSize + " 字节");
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.')+1;
        if (dotIndex >= 1 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex).toLowerCase();
        }
        return "";
    }

    /**
     * 检查文件类型是否允许
     */
    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String extension) {
        return UUID.randomUUID() +"."+extension;
    }

    /**
     * 确保目录存在
     */
    private void ensureDirectoryExists(Path path) throws IOException {
        File directory = path.toFile();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                log.error("无法创建目录: {}", path);
                throw new IOException("无法创建上传目录: " + path);
            }
            log.info("创建目录成功: {}", path);
        }
    }

    /**
     * 保存文件
     */
    private void saveFile(MultipartFile file, File dest) throws IOException {
        try {
            Files.write(dest.toPath(), file.getBytes());
            log.info("文件保存成功: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            log.error("文件保存失败: {}", dest.getAbsolutePath(), e);
            // 尝试删除不完整文件
            if (dest.exists()) {
                if (!dest.delete()) {
                    log.warn("无法删除不完整文件: {}", dest.getAbsolutePath());
                }
            }
            throw e;
        }
    }
}