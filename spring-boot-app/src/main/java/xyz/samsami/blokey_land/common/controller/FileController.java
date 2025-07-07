package xyz.samsami.blokey_land.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.service.FileService;
import xyz.samsami.blokey_land.common.type.ResultType;

@RestController
@RequiredArgsConstructor
public class FileController implements FileApi {
    private final FileService service;

    public CommonRespDto<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedFileName = service.saveFile(file);
        return CommonRespDto.of(ResultType.SUCCESS, "파일 업로드 완료", savedFileName);
    }
}
