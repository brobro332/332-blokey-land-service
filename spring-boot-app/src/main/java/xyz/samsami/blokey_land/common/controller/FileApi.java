package xyz.samsami.blokey_land.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;

@RequestMapping("/api/files")
@Tag(name = "File API", description = "파일 관련 API")
public interface FileApi {
    @PostMapping
    @Operation(summary = "파일 업로드", description = "파일을 업로드 합니다.")
    CommonRespDto<String> uploadFile(@RequestParam("file") MultipartFile file);
}
