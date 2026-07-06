package cn.iocoder.yudao.module.optical.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Optical 错误码枚举类。
 */
public interface ErrorCodeConstants {

    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(1_020_000_001, "客户不存在");
    ErrorCode OPTOMETRY_RECORD_NOT_EXISTS = new ErrorCode(1_020_000_002, "验光记录不存在");
    ErrorCode ORDER_NOT_EXISTS = new ErrorCode(1_020_000_005, "配镜订单不存在");
    ErrorCode ORDER_ITEM_NOT_EXISTS = new ErrorCode(1_020_000_006, "订单明细不存在");

}
