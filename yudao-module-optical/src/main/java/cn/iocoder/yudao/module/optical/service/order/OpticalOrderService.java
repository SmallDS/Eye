package cn.iocoder.yudao.module.optical.service.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderDO;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public interface OpticalOrderService {

    Long createOpticalOrder(@Valid OpticalOrderSaveReqVO createReqVO);
    void updateOpticalOrder(@Valid OpticalOrderSaveReqVO updateReqVO);
    void updatePaidAmount(Long orderId, BigDecimal amount);
    void deleteOpticalOrder(Long id);
    void deleteOpticalOrderList(List<Long> ids);
    OpticalOrderDO getOpticalOrder(Long id);
    PageResult<OpticalOrderDO> getOpticalOrderPage(OpticalOrderPageReqVO pageReqVO);

}
