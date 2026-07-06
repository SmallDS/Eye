package cn.iocoder.yudao.module.optical.controller.admin.customer.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 客户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CustomerRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("姓名")
    private String name;

    @Schema(description = "手机号", example = "13800138000")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "性别", example = "1")
    @ExcelProperty("性别")
    private Integer gender;

    @Schema(description = "年龄", example = "32")
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "最近验光时间")
    @ExcelProperty("最近验光时间")
    private LocalDateTime latestOptometryTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}