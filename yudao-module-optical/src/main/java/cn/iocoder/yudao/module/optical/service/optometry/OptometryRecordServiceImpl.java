package cn.iocoder.yudao.module.optical.service.optometry;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.optometry.OptometryRecordDO;
import cn.iocoder.yudao.module.optical.dal.mysql.optometry.OptometryRecordMapper;
import cn.iocoder.yudao.module.optical.service.customer.CustomerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.optical.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;
import static cn.iocoder.yudao.module.optical.enums.ErrorCodeConstants.OPTOMETRY_RECORD_NOT_EXISTS;

@Service
@Validated
public class OptometryRecordServiceImpl implements OptometryRecordService {

    @Resource
    private OptometryRecordMapper optometryRecordMapper;
    @Resource
    private CustomerService customerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOptometryRecord(OptometryRecordSaveReqVO createReqVO) {
        validateCustomerExists(createReqVO.getCustomerId());
        OptometryRecordDO record = BeanUtils.toBean(createReqVO, OptometryRecordDO.class);
        optometryRecordMapper.insert(record);
        refreshCustomerLatestOptometryTime(record.getCustomerId());
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOptometryRecord(OptometryRecordSaveReqVO updateReqVO) {
        OptometryRecordDO oldRecord = validateOptometryRecordExists(updateReqVO.getId());
        validateCustomerExists(updateReqVO.getCustomerId());
        OptometryRecordDO record = BeanUtils.toBean(updateReqVO, OptometryRecordDO.class);
        optometryRecordMapper.updateById(record);
        refreshCustomerLatestOptometryTime(oldRecord.getCustomerId());
        if (!oldRecord.getCustomerId().equals(record.getCustomerId())) {
            refreshCustomerLatestOptometryTime(record.getCustomerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOptometryRecord(Long id) {
        OptometryRecordDO record = validateOptometryRecordExists(id);
        optometryRecordMapper.deleteById(id);
        refreshCustomerLatestOptometryTime(record.getCustomerId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOptometryRecordList(List<Long> ids) {
        List<OptometryRecordDO> records = validateOptometryRecordExists(ids);
        optometryRecordMapper.deleteByIds(ids);
        records.stream().map(OptometryRecordDO::getCustomerId).distinct().forEach(this::refreshCustomerLatestOptometryTime);
    }

    private OptometryRecordDO validateOptometryRecordExists(Long id) {
        OptometryRecordDO record = optometryRecordMapper.selectById(id);
        if (record == null) {
            throw exception(OPTOMETRY_RECORD_NOT_EXISTS);
        }
        return record;
    }

    private List<OptometryRecordDO> validateOptometryRecordExists(List<Long> ids) {
        List<OptometryRecordDO> list = optometryRecordMapper.selectByIds(ids);
        if (CollUtil.isEmpty(list) || list.size() != ids.size()) {
            throw exception(OPTOMETRY_RECORD_NOT_EXISTS);
        }
        return list;
    }

    private void validateCustomerExists(Long customerId) {
        if (customerService.getCustomer(customerId) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    private void refreshCustomerLatestOptometryTime(Long customerId) {
        if (customerId == null || customerService.getCustomer(customerId) == null) {
            return;
        }
        OptometryRecordDO latest = optometryRecordMapper.selectLatestByCustomerId(customerId);
        customerService.updateLatestOptometryTime(customerId, latest == null ? null : latest.getOptometryTime());
    }

    @Override
    public OptometryRecordDO getOptometryRecord(Long id) {
        return optometryRecordMapper.selectById(id);
    }

    @Override
    public PageResult<OptometryRecordDO> getOptometryRecordPage(OptometryRecordPageReqVO pageReqVO) {
        return optometryRecordMapper.selectPage(pageReqVO);
    }

}