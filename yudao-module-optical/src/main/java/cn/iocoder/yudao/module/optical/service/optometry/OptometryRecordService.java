package cn.iocoder.yudao.module.optical.service.optometry;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.optometry.OptometryRecordDO;
import jakarta.validation.Valid;

import java.util.List;

public interface OptometryRecordService {

    Long createOptometryRecord(@Valid OptometryRecordSaveReqVO createReqVO);
    void updateOptometryRecord(@Valid OptometryRecordSaveReqVO updateReqVO);
    void deleteOptometryRecord(Long id);
    void deleteOptometryRecordList(List<Long> ids);
    OptometryRecordDO getOptometryRecord(Long id);
    PageResult<OptometryRecordDO> getOptometryRecordPage(OptometryRecordPageReqVO pageReqVO);

}
