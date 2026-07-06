package cn.iocoder.yudao.module.optical.dal.mysql.customer;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.customer.CustomerDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapperX<CustomerDO> {

    default PageResult<CustomerDO> selectPage(CustomerPageReqVO reqVO) {
        String keyword = StrUtil.trimToNull(reqVO.getKeyword());
        LambdaQueryWrapperX<CustomerDO> queryWrapper = new LambdaQueryWrapperX<CustomerDO>()
                .likeIfPresent(CustomerDO::getName, reqVO.getName())
                .likeIfPresent(CustomerDO::getMobile, reqVO.getMobile())
                .betweenIfPresent(CustomerDO::getCreateTime, reqVO.getCreateTime());
        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(CustomerDO::getName, keyword)
                    .or()
                    .like(CustomerDO::getMobile, keyword));
        }
        queryWrapper.orderByDesc(CustomerDO::getId);
        return selectPage(reqVO, queryWrapper);
    }

    default CustomerDO selectByMobile(String mobile) {
        return selectOne(new LambdaQueryWrapperX<CustomerDO>()
                .eq(CustomerDO::getMobile, mobile)
                .last("LIMIT 1"));
    }

    default List<CustomerDO> selectListByName(String name) {
        return selectList(new LambdaQueryWrapperX<CustomerDO>()
                .eq(CustomerDO::getName, name)
                .orderByAsc(CustomerDO::getId));
    }

    default void updateLatestOptometryTime(Long id, LocalDateTime latestOptometryTime) {
        update(null, new LambdaUpdateWrapper<CustomerDO>()
                .eq(CustomerDO::getId, id)
                .set(CustomerDO::getLatestOptometryTime, latestOptometryTime));
    }

}