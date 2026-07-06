package cn.iocoder.yudao.module.optical.controller.admin.order.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
public class OpticalOrderItemRespVO {

    @ExcelProperty("编号")
    private Long id;
    @ExcelProperty("订单编号")
    private Long orderId;
    @ExcelProperty("项目类型")
    private String itemType;
    private Long productId;
    @ExcelProperty("项目名称")
    private String productName;
    @ExcelProperty("单价")
    private BigDecimal unitPrice;
    @ExcelProperty("小计")
    private BigDecimal totalPrice;
    @ExcelProperty("备注")
    private String remark;
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}