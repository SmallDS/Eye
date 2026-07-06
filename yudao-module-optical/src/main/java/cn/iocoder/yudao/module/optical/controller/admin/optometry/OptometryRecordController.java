package cn.iocoder.yudao.module.optical.controller.admin.optometry;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordRespVO;
import cn.iocoder.yudao.module.optical.controller.admin.optometry.vo.OptometryRecordSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.optometry.OptometryRecordDO;
import cn.iocoder.yudao.module.optical.service.optometry.OptometryRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 验光管理")
@RestController
@RequestMapping("/opt/optometry")
@Validated
public class OptometryRecordController {

    @Resource
    private OptometryRecordService optometryRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建验光记录")
    @PreAuthorize("@ss.hasPermission('opt:optometry:create')")
    public CommonResult<Long> createOptometryRecord(@Valid @RequestBody OptometryRecordSaveReqVO createReqVO) {
        return success(optometryRecordService.createOptometryRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新验光记录")
    @PreAuthorize("@ss.hasPermission('opt:optometry:update')")
    public CommonResult<Boolean> updateOptometryRecord(@Valid @RequestBody OptometryRecordSaveReqVO updateReqVO) {
        optometryRecordService.updateOptometryRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除验光记录")
    @PreAuthorize("@ss.hasPermission('opt:optometry:delete')")
    public CommonResult<Boolean> deleteOptometryRecord(@RequestParam("id") Long id) {
        optometryRecordService.deleteOptometryRecord(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除验光记录")
    @PreAuthorize("@ss.hasPermission('opt:optometry:delete')")
    public CommonResult<Boolean> deleteOptometryRecordList(@RequestParam("ids") List<Long> ids) {
        optometryRecordService.deleteOptometryRecordList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得验光记录")
    @PreAuthorize("@ss.hasPermission('opt:optometry:query')")
    public CommonResult<OptometryRecordRespVO> getOptometryRecord(@RequestParam("id") Long id) {
        OptometryRecordDO record = optometryRecordService.getOptometryRecord(id);
        return success(BeanUtils.toBean(record, OptometryRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得验光记录分页")
    @PreAuthorize("@ss.hasPermission('opt:optometry:query')")
    public CommonResult<PageResult<OptometryRecordRespVO>> getOptometryRecordPage(@Valid OptometryRecordPageReqVO pageReqVO) {
        PageResult<OptometryRecordDO> pageResult = optometryRecordService.getOptometryRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OptometryRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出验光记录 Excel")
    @PreAuthorize("@ss.hasPermission('opt:optometry:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOptometryRecordExcel(@Valid OptometryRecordPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OptometryRecordDO> list = optometryRecordService.getOptometryRecordPage(pageReqVO).getList();
        ExcelUtils.write(response, "验光记录.xls", "数据", OptometryRecordRespVO.class, BeanUtils.toBean(list, OptometryRecordRespVO.class));
    }

}