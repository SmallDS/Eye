package cn.iocoder.yudao.module.optical.service.importdata;

import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportCustomerExcelVO;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportOptometryExcelVO;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportRespVO;

import java.util.List;

public interface OptImportService {

    OptImportRespVO importData(List<OptImportCustomerExcelVO> customers,
                               List<OptImportOptometryExcelVO> optometryRecords,
                               boolean updateSupport);

}