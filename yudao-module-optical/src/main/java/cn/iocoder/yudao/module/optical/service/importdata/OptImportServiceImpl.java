package cn.iocoder.yudao.module.optical.service.importdata;

import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportCustomerExcelVO;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportOptometryExcelVO;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportRespVO;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.module.optical.dal.dataobject.optometry.OptometryRecordDO;
import cn.iocoder.yudao.module.optical.dal.mysql.customer.CustomerMapper;
import cn.iocoder.yudao.module.optical.dal.mysql.optometry.OptometryRecordMapper;
import cn.iocoder.yudao.module.optical.service.optometry.OptometryRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class OptImportServiceImpl implements OptImportService {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private OptometryRecordMapper optometryRecordMapper;
    @Resource
    private OptometryRecordService optometryRecordService;

    @Override
    public OptImportRespVO importData(List<OptImportCustomerExcelVO> customers,
                                      List<OptImportOptometryExcelVO> optometryRecords,
                                      boolean updateSupport) {
        OptImportRespVO respVO = OptImportRespVO.builder()
                .createNames(new ArrayList<>())
                .updateNames(new ArrayList<>())
                .failureNames(new LinkedHashMap<>())
                .build();
        importCustomers(customers, updateSupport, respVO);
        importOptometryRecords(optometryRecords, updateSupport, respVO);
        return respVO;
    }

    private void importCustomers(List<OptImportCustomerExcelVO> customers, boolean updateSupport, OptImportRespVO respVO) {
        for (int i = 0; i < customers.size(); i++) {
            OptImportCustomerExcelVO item = customers.get(i);
            String rowKey = "客户第" + (i + 2) + "行";
            try {
                if (item == null || isBlank(item.getName())) {
                    fail(respVO, rowKey, "姓名不能为空");
                    continue;
                }
                Integer gender = parseGender(item.getGender());
                if (item.getAge() != null && (item.getAge() < 0 || item.getAge() > 150)) {
                    fail(respVO, rowKey, "年龄必须在 0-150 之间");
                    continue;
                }
                CustomerDO existCustomer = item.getId() != null ? customerMapper.selectById(item.getId()) : (isBlank(item.getMobile()) ? null : customerMapper.selectByMobile(trim(item.getMobile())));
                if (existCustomer != null && !updateSupport) {
                    fail(respVO, rowKey, "客户编号或手机号已存在");
                    continue;
                }
                if (existCustomer == null) {
                    CustomerDO customer = new CustomerDO();
                    customer.setId(item.getId());
                    fillCustomer(customer, item, gender);
                    customerMapper.insert(customer);
                    respVO.getCreateNames().add("客户:" + customer.getName());
                } else {
                    fillCustomer(existCustomer, item, gender);
                    customerMapper.updateById(existCustomer);
                    respVO.getUpdateNames().add("客户:" + existCustomer.getName());
                }
            } catch (Exception ex) {
                fail(respVO, rowKey, ex.getMessage());
            }
        }
    }

    private void importOptometryRecords(List<OptImportOptometryExcelVO> records, boolean updateSupport, OptImportRespVO respVO) {
        for (int i = 0; i < records.size(); i++) {
            OptImportOptometryExcelVO item = records.get(i);
            String rowKey = "验光单第" + (i + 2) + "行";
            try {
                if (item == null || (item.getCustomerId() == null && isBlank(item.getCustomerMobile()) && isBlank(item.getCustomerName()))) {
                    fail(respVO, rowKey, "\u5ba2\u6237\u7f16\u53f7\u3001\u5ba2\u6237\u624b\u673a\u53f7\u3001\u5ba2\u6237\u59d3\u540d\u81f3\u5c11\u586b\u5199\u4e00\u4e2a");
                    continue;
                }
                if (item.getOptometryTime() == null) {
                    fail(respVO, rowKey, "验光时间不能为空");
                    continue;
                }
                CustomerDO customer = findCustomer(item);
                if (customer == null) {
                    fail(respVO, rowKey, "客户不存在");
                    continue;
                }
                OptometryRecordDO existRecord = optometryRecordMapper.selectByCustomerIdAndOptometryTime(customer.getId(), item.getOptometryTime());
                if (existRecord != null && !updateSupport) {
                    fail(respVO, rowKey, "该客户相同验光时间的验光单已存在");
                    continue;
                }
                OptometryRecordSaveReqVO saveReqVO = buildOptometrySaveReqVO(item, customer.getId());
                if (existRecord == null) {
                    optometryRecordService.createOptometryRecord(saveReqVO);
                    respVO.getCreateNames().add("验光单:" + customer.getName() + "/" + item.getOptometryTime());
                } else {
                    saveReqVO.setId(existRecord.getId());
                    optometryRecordService.updateOptometryRecord(saveReqVO);
                    respVO.getUpdateNames().add("验光单:" + customer.getName() + "/" + item.getOptometryTime());
                }
            } catch (Exception ex) {
                fail(respVO, rowKey, ex.getMessage());
            }
        }
    }

    private CustomerDO findCustomer(OptImportOptometryExcelVO item) {
        if (item.getCustomerId() != null) {
            return customerMapper.selectById(item.getCustomerId());
        }
        if (isNotBlank(item.getCustomerMobile())) {
            return customerMapper.selectByMobile(trim(item.getCustomerMobile()));
        }
        List<CustomerDO> customers = customerMapper.selectListByName(trim(item.getCustomerName()));
        if (customers.isEmpty()) {
            return null;
        }
        if (customers.size() > 1) {
            throw new IllegalArgumentException("\u5ba2\u6237\u59d3\u540d\u5339\u914d\u5230\u591a\u4e2a\u5ba2\u6237\uff0c\u8bf7\u586b\u5199\u5ba2\u6237\u7f16\u53f7\u6216\u5ba2\u6237\u624b\u673a\u53f7");
        }
        return customers.get(0);
    }

    private void fillCustomer(CustomerDO customer, OptImportCustomerExcelVO item, Integer gender) {
        customer.setName(trim(item.getName()));
        customer.setMobile(trimToNull(item.getMobile()));
        customer.setGender(gender);
        customer.setAge(item.getAge());
        customer.setRemark(trimToNull(item.getRemark()));
    }

    private OptometryRecordSaveReqVO buildOptometrySaveReqVO(OptImportOptometryExcelVO item, Long customerId) {
        OptometryRecordSaveReqVO vo = new OptometryRecordSaveReqVO();
        vo.setCustomerId(customerId);
        vo.setOptometryTime(item.getOptometryTime());
        vo.setRemark(trimToNull(item.getRemark()));
        vo.setDistanceRightSph(parseDecimal(item.getDistanceRightSph(), "DistanceRightSph"));
        vo.setDistanceRightCyl(parseDecimal(item.getDistanceRightCyl(), "DistanceRightCyl"));
        vo.setDistanceRightAxis(parseDecimal(item.getDistanceRightAxis(), "DistanceRightAxis"));
        vo.setDistanceRightPrism(parseDecimal(item.getDistanceRightPrism(), "DistanceRightPrism"));
        vo.setDistanceRightBase(trimToNull(item.getDistanceRightBase()));
        vo.setDistanceRightAddition(parseDecimal(item.getDistanceRightAddition(), "DistanceRightAddition"));
        vo.setDistanceRightBaseCurveV(parseDecimal(item.getDistanceRightBaseCurveV(), "DistanceRightBaseCurveV"));
        vo.setDistanceRightBaseCurveH(parseDecimal(item.getDistanceRightBaseCurveH(), "DistanceRightBaseCurveH"));
        vo.setDistanceRightDiameter(parseDecimal(item.getDistanceRightDiameter(), "DistanceRightDiameter"));
        vo.setDistanceRightNakedVision(parseDecimal(item.getDistanceRightNakedVision(), "DistanceRightNakedVision"));
        vo.setDistanceRightCorrectedVision(parseDecimal(item.getDistanceRightCorrectedVision(), "DistanceRightCorrectedVision"));
        vo.setDistanceLeftSph(parseDecimal(item.getDistanceLeftSph(), "DistanceLeftSph"));
        vo.setDistanceLeftCyl(parseDecimal(item.getDistanceLeftCyl(), "DistanceLeftCyl"));
        vo.setDistanceLeftAxis(parseDecimal(item.getDistanceLeftAxis(), "DistanceLeftAxis"));
        vo.setDistanceLeftPrism(parseDecimal(item.getDistanceLeftPrism(), "DistanceLeftPrism"));
        vo.setDistanceLeftBase(trimToNull(item.getDistanceLeftBase()));
        vo.setDistanceLeftAddition(parseDecimal(item.getDistanceLeftAddition(), "DistanceLeftAddition"));
        vo.setDistanceLeftBaseCurveV(parseDecimal(item.getDistanceLeftBaseCurveV(), "DistanceLeftBaseCurveV"));
        vo.setDistanceLeftBaseCurveH(parseDecimal(item.getDistanceLeftBaseCurveH(), "DistanceLeftBaseCurveH"));
        vo.setDistanceLeftDiameter(parseDecimal(item.getDistanceLeftDiameter(), "DistanceLeftDiameter"));
        vo.setDistanceLeftNakedVision(parseDecimal(item.getDistanceLeftNakedVision(), "DistanceLeftNakedVision"));
        vo.setDistanceLeftCorrectedVision(parseDecimal(item.getDistanceLeftCorrectedVision(), "DistanceLeftCorrectedVision"));
        vo.setNearRightSph(parseDecimal(item.getNearRightSph(), "NearRightSph"));
        vo.setNearRightCyl(parseDecimal(item.getNearRightCyl(), "NearRightCyl"));
        vo.setNearRightAxis(parseDecimal(item.getNearRightAxis(), "NearRightAxis"));
        vo.setNearRightPrism(parseDecimal(item.getNearRightPrism(), "NearRightPrism"));
        vo.setNearRightBase(trimToNull(item.getNearRightBase()));
        vo.setNearRightAddition(parseDecimal(item.getNearRightAddition(), "NearRightAddition"));
        vo.setNearRightBaseCurveV(parseDecimal(item.getNearRightBaseCurveV(), "NearRightBaseCurveV"));
        vo.setNearRightBaseCurveH(parseDecimal(item.getNearRightBaseCurveH(), "NearRightBaseCurveH"));
        vo.setNearRightDiameter(parseDecimal(item.getNearRightDiameter(), "NearRightDiameter"));
        vo.setNearRightNakedVision(parseDecimal(item.getNearRightNakedVision(), "NearRightNakedVision"));
        vo.setNearRightCorrectedVision(parseDecimal(item.getNearRightCorrectedVision(), "NearRightCorrectedVision"));
        vo.setNearLeftSph(parseDecimal(item.getNearLeftSph(), "NearLeftSph"));
        vo.setNearLeftCyl(parseDecimal(item.getNearLeftCyl(), "NearLeftCyl"));
        vo.setNearLeftAxis(parseDecimal(item.getNearLeftAxis(), "NearLeftAxis"));
        vo.setNearLeftPrism(parseDecimal(item.getNearLeftPrism(), "NearLeftPrism"));
        vo.setNearLeftBase(trimToNull(item.getNearLeftBase()));
        vo.setNearLeftAddition(parseDecimal(item.getNearLeftAddition(), "NearLeftAddition"));
        vo.setNearLeftBaseCurveV(parseDecimal(item.getNearLeftBaseCurveV(), "NearLeftBaseCurveV"));
        vo.setNearLeftBaseCurveH(parseDecimal(item.getNearLeftBaseCurveH(), "NearLeftBaseCurveH"));
        vo.setNearLeftDiameter(parseDecimal(item.getNearLeftDiameter(), "NearLeftDiameter"));
        vo.setNearLeftNakedVision(parseDecimal(item.getNearLeftNakedVision(), "NearLeftNakedVision"));
        vo.setNearLeftCorrectedVision(parseDecimal(item.getNearLeftCorrectedVision(), "NearLeftCorrectedVision"));
        vo.setDistancePd(parseDecimal(item.getDistancePd(), "DistancePd"));
        vo.setRpd(parseDecimal(item.getRpd(), "Rpd"));
        vo.setLpd(parseDecimal(item.getLpd(), "Lpd"));
        vo.setNearPd(parseDecimal(item.getNearPd(), "NearPd"));
        vo.setRh(parseDecimal(item.getRh(), "Rh"));
        vo.setLh(parseDecimal(item.getLh(), "Lh"));
        return vo;
    }

    private BigDecimal parseDecimal(String value, String fieldName) {
        if (isBlank(value)) {
            return null;
        }
        String text = trim(value)
                .replace("＋", "+")
                .replace("－", "-")
                .replace("，", ",")
                .replace(" ", "")
                .replace("　", "")
                .replace("\u00A0", "");
        if (text.startsWith("+")) {
            text = text.substring(1);
        }
        if ("平光".equalsIgnoreCase(text) || "PL".equalsIgnoreCase(text) || "PLANO".equalsIgnoreCase(text)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(text.replace(",", ""));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + "必须是数字");
        }
    }

    private Integer parseGender(String value) {
        if (isBlank(value)) {
            return null;
        }
        String text = trim(value);
        if ("男".equals(text) || "1".equals(text)) {
            return 1;
        }
        if ("女".equals(text) || "2".equals(text)) {
            return 2;
        }
        throw new IllegalArgumentException("性别只支持 男/女 或 1/2");
    }

    private void fail(OptImportRespVO respVO, String key, String reason) {
        respVO.getFailureNames().put(key, isBlank(reason) ? "导入失败" : reason);
    }

    private boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String trimToNull(String value) {
        String text = trim(value);
        return isBlank(text) ? null : text;
    }

}