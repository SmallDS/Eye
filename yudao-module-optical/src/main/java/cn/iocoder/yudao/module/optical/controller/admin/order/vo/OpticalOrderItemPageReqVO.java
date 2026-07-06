package cn.iocoder.yudao.module.optical.controller.admin.order.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class OpticalOrderItemPageReqVO extends PageParam {

    private Long orderId;
    private String itemType;
    private Long productId;

}