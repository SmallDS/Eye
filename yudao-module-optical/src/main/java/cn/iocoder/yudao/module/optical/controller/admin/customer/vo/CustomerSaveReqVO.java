package cn.iocoder.yudao.module.optical.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 客户新增/修改 Request VO")
@Data
public class CustomerSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "性别", example = "1")
    private Integer gender;

    @Schema(description = "年龄", example = "32")
    @Min(value = 0, message = "年龄不能小于 0")
    @Max(value = 150, message = "年龄不能大于 150")
    private Integer age;

    @Schema(description = "备注")
    private String remark;

}