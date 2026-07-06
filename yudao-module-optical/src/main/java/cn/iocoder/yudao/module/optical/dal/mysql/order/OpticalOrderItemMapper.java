package cn.iocoder.yudao.module.optical.dal.mysql.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemPageReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface OpticalOrderItemMapper extends BaseMapperX<OpticalOrderItemDO> {

    default PageResult<OpticalOrderItemDO> selectPage(OpticalOrderItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OpticalOrderItemDO>()
                .eqIfPresent(OpticalOrderItemDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(OpticalOrderItemDO::getItemType, reqVO.getItemType())
                .eqIfPresent(OpticalOrderItemDO::getProductId, reqVO.getProductId())
                .orderByDesc(OpticalOrderItemDO::getId));
    }

    default List<OpticalOrderItemDO> selectListByOrderId(Long orderId) {
        return selectList(new LambdaQueryWrapperX<OpticalOrderItemDO>()
                .eq(OpticalOrderItemDO::getOrderId, orderId)
                .orderByAsc(OpticalOrderItemDO::getId));
    }

    default List<OpticalOrderItemDO> selectSuggestList(String itemType, String keyword) {
        LambdaQueryWrapperX<OpticalOrderItemDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.eqIfPresent(OpticalOrderItemDO::getItemType, itemType);
        wrapper.likeIfPresent(OpticalOrderItemDO::getProductName, keyword);
        wrapper.isNotNull(OpticalOrderItemDO::getProductName);
        wrapper.orderByDesc(OpticalOrderItemDO::getCreateTime);
        wrapper.last("LIMIT 50");
        return selectList(wrapper);
    }

    default void deleteByOrderId(Long orderId) {
        delete(new LambdaQueryWrapperX<OpticalOrderItemDO>()
                .eq(OpticalOrderItemDO::getOrderId, orderId));
    }

    default void deleteByOrderIds(Collection<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapperX<OpticalOrderItemDO>()
                .in(OpticalOrderItemDO::getOrderId, orderIds));
    }

}