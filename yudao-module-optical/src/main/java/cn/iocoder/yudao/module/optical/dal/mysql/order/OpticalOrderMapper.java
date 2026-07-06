package cn.iocoder.yudao.module.optical.dal.mysql.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderPageReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface OpticalOrderMapper extends BaseMapperX<OpticalOrderDO> {

    default PageResult<OpticalOrderDO> selectPage(OpticalOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OpticalOrderDO>()
                .likeIfPresent(OpticalOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(OpticalOrderDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(OpticalOrderDO::getOptometryRecordId, reqVO.getOptometryRecordId())
                .betweenIfPresent(OpticalOrderDO::getOrderTime, reqVO.getOrderTime())
                .betweenIfPresent(OpticalOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OpticalOrderDO::getId));
    }

    default List<OpticalOrderDO> selectListByCustomerIds(Collection<Long> customerIds) {
        if (customerIds == null || customerIds.isEmpty()) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapperX<OpticalOrderDO>()
                .in(OpticalOrderDO::getCustomerId, customerIds));
    }

    default void deleteByCustomerIds(Collection<Long> customerIds) {
        if (customerIds == null || customerIds.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapperX<OpticalOrderDO>()
                .in(OpticalOrderDO::getCustomerId, customerIds));
    }

}
