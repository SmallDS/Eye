package cn.iocoder.yudao.module.optical.service.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderItemDO;
import cn.iocoder.yudao.module.optical.dal.mysql.order.OpticalOrderItemMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.invalidParamException;
import static cn.iocoder.yudao.module.optical.enums.ErrorCodeConstants.ORDER_ITEM_NOT_EXISTS;

@Service
@Validated
public class OpticalOrderItemServiceImpl implements OpticalOrderItemService {

    @Resource
    private OpticalOrderItemMapper opticalOrderItemMapper;

    @Override
    public Long createOpticalOrderItem(OpticalOrderItemSaveReqVO createReqVO) {
        validateStandaloneItemOrderId(createReqVO.getOrderId());
        OpticalOrderItemDO item = BeanUtils.toBean(createReqVO, OpticalOrderItemDO.class);
        normalizeItem(item);
        opticalOrderItemMapper.insert(item);
        return item.getId();
    }

    @Override
    public void updateOpticalOrderItem(OpticalOrderItemSaveReqVO updateReqVO) {
        validateStandaloneItemOrderId(updateReqVO.getOrderId());
        validateOpticalOrderItemExists(updateReqVO.getId());
        OpticalOrderItemDO item = BeanUtils.toBean(updateReqVO, OpticalOrderItemDO.class);
        normalizeItem(item);
        opticalOrderItemMapper.updateById(item);
    }

    @Override
    public void deleteOpticalOrderItem(Long id) {
        validateOpticalOrderItemExists(id);
        opticalOrderItemMapper.deleteById(id);
    }

    @Override
    public void deleteOpticalOrderItemList(List<Long> ids) {
        validateOpticalOrderItemExists(ids);
        opticalOrderItemMapper.deleteByIds(ids);
    }

    private void validateStandaloneItemOrderId(Long orderId) {
        if (orderId == null) {
            throw invalidParamException("订单不能为空");
        }
    }

    private void validateOpticalOrderItemExists(Long id) {
        if (opticalOrderItemMapper.selectById(id) == null) {
            throw exception(ORDER_ITEM_NOT_EXISTS);
        }
    }

    private void validateOpticalOrderItemExists(List<Long> ids) {
        List<OpticalOrderItemDO> list = opticalOrderItemMapper.selectByIds(ids);
        if (CollUtil.isEmpty(list) || list.size() != ids.size()) {
            throw exception(ORDER_ITEM_NOT_EXISTS);
        }
    }

    private void normalizeItem(OpticalOrderItemDO item) {
        item.setQuantity(1);
        if (item.getUnitPrice() == null) {
            item.setUnitPrice(BigDecimal.ZERO);
        }
        item.setTotalPrice(item.getUnitPrice());
        item.setProductName(StrUtil.trimToNull(item.getProductName()));
        item.setItemType(StrUtil.trimToNull(item.getItemType()));
    }

    @Override
    public OpticalOrderItemDO getOpticalOrderItem(Long id) {
        return opticalOrderItemMapper.selectById(id);
    }

    @Override
    public List<OpticalOrderItemDO> getOpticalOrderItemListByOrderId(Long orderId) {
        return opticalOrderItemMapper.selectListByOrderId(orderId);
    }

    @Override
    public List<String> getProductNameSuggestList(String itemType, String keyword) {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (OpticalOrderItemDO item : opticalOrderItemMapper.selectSuggestList(StrUtil.trimToNull(itemType), StrUtil.trimToNull(keyword))) {
            if (StrUtil.isNotBlank(item.getProductName())) {
                names.add(item.getProductName());
            }
            if (names.size() >= 20) {
                break;
            }
        }
        return List.copyOf(names);
    }

    @Override
    public PageResult<OpticalOrderItemDO> getOpticalOrderItemPage(OpticalOrderItemPageReqVO pageReqVO) {
        return opticalOrderItemMapper.selectPage(pageReqVO);
    }

}