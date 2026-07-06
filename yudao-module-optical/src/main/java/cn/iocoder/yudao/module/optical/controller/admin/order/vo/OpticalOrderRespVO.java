package cn.iocoder.yudao.module.optical.controller.admin.order.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ExcelIgnoreUnannotated
public class OpticalOrderRespVO {

    @ExcelProperty("编号")
    private Long id;
    @ExcelProperty("订单号")
    private String orderNo;
    @ExcelProperty("客户编号")
    private Long customerId;
    @ExcelProperty("验光记录")
    private Long optometryRecordId;
    @ExcelProperty("总金额")
    private BigDecimal totalAmount;
    @ExcelProperty("优惠金额")
    private BigDecimal discountAmount;
    @ExcelProperty("应收金额")
    private BigDecimal receivableAmount;
    @ExcelProperty("已付金额")
    private BigDecimal paidAmount;
    @ExcelProperty("下单时间")
    private LocalDateTime orderTime;
    @ExcelProperty("取镜时间")
    private LocalDateTime pickupTime;
    @ExcelProperty("备注")
    private String remark;
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private List<OpticalOrderItemRespVO> items;

}
