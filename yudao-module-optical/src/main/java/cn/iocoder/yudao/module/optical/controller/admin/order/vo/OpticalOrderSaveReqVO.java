package cn.iocoder.yudao.module.optical.controller.admin.order.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OpticalOrderSaveReqVO {

    private Long id;
    private String orderNo;

    @NotNull(message = "Customer cannot be empty")
    private Long customerId;

    private Long optometryRecordId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal receivableAmount;
    private BigDecimal paidAmount;
    private LocalDateTime orderTime;
    private LocalDateTime pickupTime;
    private String remark;

    @Valid
    private List<OpticalOrderItemSaveReqVO> items;

}
