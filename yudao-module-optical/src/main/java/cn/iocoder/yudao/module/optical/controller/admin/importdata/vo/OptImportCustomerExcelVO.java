package cn.iocoder.yudao.module.optical.controller.admin.importdata.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptImportCustomerExcelVO {

    @ExcelProperty("\u5ba2\u6237\u7f16\u53f7")
    private Long id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("备注")
    private String remark;

}