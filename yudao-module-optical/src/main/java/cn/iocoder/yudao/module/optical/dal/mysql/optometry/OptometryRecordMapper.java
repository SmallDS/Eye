package cn.iocoder.yudao.module.optical.dal.mysql.optometry;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordPageReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.optometry.OptometryRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;

@Mapper
public interface OptometryRecordMapper extends BaseMapperX<OptometryRecordDO> {

    default PageResult<OptometryRecordDO> selectPage(OptometryRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OptometryRecordDO>()
                .eqIfPresent(OptometryRecordDO::getCustomerId, reqVO.getCustomerId())
                .betweenIfPresent(OptometryRecordDO::getOptometryTime, reqVO.getOptometryTime())
                .betweenIfPresent(OptometryRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OptometryRecordDO::getOptometryTime)
                .orderByDesc(OptometryRecordDO::getId));
    }

    default OptometryRecordDO selectLatestByCustomerId(Long customerId) {
        return selectOne(new LambdaQueryWrapperX<OptometryRecordDO>()
                .eq(OptometryRecordDO::getCustomerId, customerId)
                .orderByDesc(OptometryRecordDO::getOptometryTime)
                .orderByDesc(OptometryRecordDO::getId)
                .last("LIMIT 1"));
    }

    default OptometryRecordDO selectByCustomerIdAndOptometryTime(Long customerId, LocalDateTime optometryTime) {
        return selectOne(new LambdaQueryWrapperX<OptometryRecordDO>()
                .eq(OptometryRecordDO::getCustomerId, customerId)
                .eq(OptometryRecordDO::getOptometryTime, optometryTime)
                .last("LIMIT 1"));
    }

    default void deleteByCustomerIds(Collection<Long> customerIds) {
        if (customerIds == null || customerIds.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapperX<OptometryRecordDO>()
                .in(OptometryRecordDO::getCustomerId, customerIds));
    }

}