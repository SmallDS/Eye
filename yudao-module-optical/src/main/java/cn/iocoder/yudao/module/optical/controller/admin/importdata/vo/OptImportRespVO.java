package cn.iocoder.yudao.module.optical.controller.admin.importdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 东方镜通导入 Response VO")
@Data
@Builder
public class OptImportRespVO {

    @Schema(description = "创建成功的数据")
    private List<String> createNames;

    @Schema(description = "更新成功的数据")
    private List<String> updateNames;

    @Schema(description = "导入失败的数据，key 为行标识，value 为失败原因")
    private Map<String, String> failureNames;

}