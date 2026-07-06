package cn.iocoder.yudao.module.optical.service.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemSaveReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderDO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderItemDO;
import cn.iocoder.yudao.module.optical.dal.mysql.order.OpticalOrderItemMapper;
import cn.iocoder.yudao.module.optical.dal.mysql.order.OpticalOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.optical.enums.ErrorCodeConstants.ORDER_NOT_EXISTS;

@Service
@Validated
public class OpticalOrderServiceImpl implements OpticalOrderService {

    @Resource
    private OpticalOrderMapper opticalOrderMapper;
    @Resource
    private OpticalOrderItemMapper opticalOrderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOpticalOrder(OpticalOrderSaveReqVO createReqVO) {
        OpticalOrderDO order = BeanUtils.toBean(createReqVO, OpticalOrderDO.class);
        normalizeOrder(order, createReqVO.getItems());
        opticalOrderMapper.insert(order);
        saveItems(order.getId(), createReqVO.getItems());
        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOpticalOrder(OpticalOrderSaveReqVO updateReqVO) {
        validateOpticalOrderExists(updateReqVO.getId());
        OpticalOrderDO order = BeanUtils.toBean(updateReqVO, OpticalOrderDO.class);
        normalizeOrder(order, updateReqVO.getItems());
        opticalOrderMapper.updateById(order);
        if (updateReqVO.getItems() != null) {
            opticalOrderItemMapper.deleteByOrderId(order.getId());
            saveItems(order.getId(), updateReqVO.getItems());
        }
    }

    @Override
    public void updatePaidAmount(Long orderId, BigDecimal amount) {
        OpticalOrderDO order = validateOpticalOrderExists(orderId);
        BigDecimal paidAmount = safe(order.getPaidAmount()).add(safe(amount));
        OpticalOrderDO updateObj = new OpticalOrderDO();
        updateObj.setId(orderId);
        updateObj.setPaidAmount(paidAmount);
        opticalOrderMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOpticalOrder(Long id) {
        validateOpticalOrderExists(id);
        opticalOrderItemMapper.deleteByOrderId(id);
        opticalOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOpticalOrderList(List<Long> ids) {
        validateOpticalOrderExists(ids);
        opticalOrderItemMapper.deleteByOrderIds(ids);
        opticalOrderMapper.deleteByIds(ids);
    }

    private void normalizeOrder(OpticalOrderDO order, List<OpticalOrderItemSaveReqVO> items) {
        if (StrUtil.isBlank(order.getOrderNo())) {
            order.setOrderNo("PJ" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        }
        if (order.getOrderTime() == null) {
            order.setOrderTime(LocalDateTime.now());
        }
        BigDecimal itemTotal = calculateItemTotal(items);
        if (CollUtil.isNotEmpty(items)) {
            order.setTotalAmount(itemTotal);
        } else if (order.getTotalAmount() == null) {
            order.setTotalAmount(BigDecimal.ZERO);
        }
        if (order.getDiscountAmount() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
        }
        order.setReceivableAmount(safe(order.getTotalAmount()).subtract(safe(order.getDiscountAmount())));
        if (order.getReceivableAmount().compareTo(BigDecimal.ZERO) < 0) {
            order.setReceivableAmount(BigDecimal.ZERO);
        }
        if (order.getPaidAmount() == null) {
            order.setPaidAmount(BigDecimal.ZERO);
        }
    }

    private BigDecimal calculateItemTotal(List<OpticalOrderItemSaveReqVO> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (CollUtil.isEmpty(items)) {
            return total;
        }
        for (OpticalOrderItemSaveReqVO item : items) {
            if (isBlankItem(item)) {
                continue;
            }
            BigDecimal unitPrice = safe(item.getUnitPrice());
            total = total.add(unitPrice);
        }
        return total;
    }

    private void saveItems(Long orderId, List<OpticalOrderItemSaveReqVO> items) {
        if (CollUtil.isEmpty(items)) {
            return;
        }
        List<OpticalOrderItemDO> itemList = new ArrayList<>();
        for (OpticalOrderItemSaveReqVO reqVO : items) {
            if (isBlankItem(reqVO)) {
                continue;
            }
            OpticalOrderItemDO item = BeanUtils.toBean(reqVO, OpticalOrderItemDO.class);
            item.setId(null);
            item.setOrderId(orderId);
            item.setProductName(StrUtil.trimToNull(item.getProductName()));
            item.setItemType(StrUtil.trimToNull(item.getItemType()));
            item.setProductId(null);
            item.setQuantity(1);
            if (item.getUnitPrice() == null) {
                item.setUnitPrice(BigDecimal.ZERO);
            }
            item.setTotalPrice(item.getUnitPrice());
            itemList.add(item);
        }
        for (OpticalOrderItemDO item : itemList) {
            opticalOrderItemMapper.insert(item);
        }
    }

    private boolean isBlankItem(OpticalOrderItemSaveReqVO item) {
        return item == null || (StrUtil.isBlank(item.getProductName()) && safe(item.getUnitPrice()).compareTo(BigDecimal.ZERO) == 0);
    }

    private OpticalOrderDO validateOpticalOrderExists(Long id) {
        OpticalOrderDO order = opticalOrderMapper.selectById(id);
        if (order == null) {
            throw exception(ORDER_NOT_EXISTS);
        }
        return order;
    }

    private List<OpticalOrderDO> validateOpticalOrderExists(List<Long> ids) {
        List<OpticalOrderDO> list = opticalOrderMapper.selectByIds(ids);
        if (CollUtil.isEmpty(list) || list.size() != ids.size()) {
            throw exception(ORDER_NOT_EXISTS);
        }
        return list;
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    public OpticalOrderDO getOpticalOrder(Long id) {
        return opticalOrderMapper.selectById(id);
    }

    @Override
    public PageResult<OpticalOrderDO> getOpticalOrderPage(OpticalOrderPageReqVO pageReqVO) {
        return opticalOrderMapper.selectPage(pageReqVO);
    }

}
