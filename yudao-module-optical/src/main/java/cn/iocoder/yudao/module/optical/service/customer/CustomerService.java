package cn.iocoder.yudao.module.optical.service.customer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.customer.CustomerDO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {

    Long createCustomer(@Valid CustomerSaveReqVO createReqVO);
    void updateCustomer(@Valid CustomerSaveReqVO updateReqVO);
    void deleteCustomer(Long id);
    void deleteCustomerList(List<Long> ids);
    CustomerDO getCustomer(Long id);
    PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO);
    void updateLatestOptometryTime(Long id, LocalDateTime latestOptometryTime);

}