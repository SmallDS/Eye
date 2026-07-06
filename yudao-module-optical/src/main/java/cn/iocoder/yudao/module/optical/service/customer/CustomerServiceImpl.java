package cn.iocoder.yudao.module.optical.service.customer;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderDO;
import cn.iocoder.yudao.module.optical.dal.mysql.customer.CustomerMapper;
import cn.iocoder.yudao.module.optical.dal.mysql.optometry.OptometryRecordMapper;
import cn.iocoder.yudao.module.optical.dal.mysql.order.OpticalOrderItemMapper;
import cn.iocoder.yudao.module.optical.dal.mysql.order.OpticalOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.optical.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;

@Service
@Validated
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private OptometryRecordMapper optometryRecordMapper;
    @Resource
    private OpticalOrderMapper opticalOrderMapper;
    @Resource
    private OpticalOrderItemMapper opticalOrderItemMapper;

    @Override
    public Long createCustomer(CustomerSaveReqVO createReqVO) {
        CustomerDO customer = BeanUtils.toBean(createReqVO, CustomerDO.class);
        customerMapper.insert(customer);
        return customer.getId();
    }

    @Override
    public void updateCustomer(CustomerSaveReqVO updateReqVO) {
        validateCustomerExists(updateReqVO.getId());
        customerMapper.updateById(BeanUtils.toBean(updateReqVO, CustomerDO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(Long id) {
        validateCustomerExists(id);
        deleteCustomerRelations(List.of(id));
        customerMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerList(List<Long> ids) {
        validateCustomerExists(ids);
        deleteCustomerRelations(ids);
        customerMapper.deleteByIds(ids);
    }

    private void deleteCustomerRelations(List<Long> customerIds) {
        List<Long> orderIds = opticalOrderMapper.selectListByCustomerIds(customerIds).stream()
                .map(OpticalOrderDO::getId)
                .toList();
        if (CollUtil.isNotEmpty(orderIds)) {
            opticalOrderItemMapper.deleteByOrderIds(orderIds);
        }
        opticalOrderMapper.deleteByCustomerIds(customerIds);
        optometryRecordMapper.deleteByCustomerIds(customerIds);
    }

    private void validateCustomerExists(Long id) {
        if (customerMapper.selectById(id) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    private void validateCustomerExists(List<Long> ids) {
        List<CustomerDO> list = customerMapper.selectByIds(ids);
        if (CollUtil.isEmpty(list) || list.size() != ids.size()) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    @Override
    public CustomerDO getCustomer(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO) {
        return customerMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateLatestOptometryTime(Long id, LocalDateTime latestOptometryTime) {
        validateCustomerExists(id);
        customerMapper.updateLatestOptometryTime(id, latestOptometryTime);
    }

}