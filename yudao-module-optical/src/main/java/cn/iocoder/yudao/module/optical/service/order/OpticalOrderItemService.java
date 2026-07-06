package cn.iocoder.yudao.module.optical.service.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderItemDO;
import jakarta.validation.Valid;

import java.util.List;

public interface OpticalOrderItemService {

    Long createOpticalOrderItem(@Valid OpticalOrderItemSaveReqVO createReqVO);
    void updateOpticalOrderItem(@Valid OpticalOrderItemSaveReqVO updateReqVO);
    void deleteOpticalOrderItem(Long id);
    void deleteOpticalOrderItemList(List<Long> ids);
    OpticalOrderItemDO getOpticalOrderItem(Long id);
    List<OpticalOrderItemDO> getOpticalOrderItemListByOrderId(Long orderId);
    List<String> getProductNameSuggestList(String itemType, String keyword);
    PageResult<OpticalOrderItemDO> getOpticalOrderItemPage(OpticalOrderItemPageReqVO pageReqVO);

}