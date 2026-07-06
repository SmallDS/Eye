package cn.iocoder.yudao.module.optical.controller.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpticalOrderItemSaveReqVO {

    private Long id;
    private Long orderId;

    private String itemType;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String remark;

}