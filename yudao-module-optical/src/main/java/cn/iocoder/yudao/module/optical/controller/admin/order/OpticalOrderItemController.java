package cn.iocoder.yudao.module.optical.controller.admin.order;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemRespVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderItemDO;
import cn.iocoder.yudao.module.optical.service.order.OpticalOrderItemService;
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

@Tag(name = "管理后台 - 配镜订单明细")
@RestController
@RequestMapping("/opt/order-item")
@Validated
public class OpticalOrderItemController {

    @Resource
    private OpticalOrderItemService opticalOrderItemService;

    @PostMapping("/create")
    @Operation(summary = "创建配镜订单明细")
    @PreAuthorize("@ss.hasPermission('opt:order:create')")
    public CommonResult<Long> createOpticalOrderItem(@Valid @RequestBody OpticalOrderItemSaveReqVO createReqVO) {
        return success(opticalOrderItemService.createOpticalOrderItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新配镜订单明细")
    @PreAuthorize("@ss.hasPermission('opt:order:update')")
    public CommonResult<Boolean> updateOpticalOrderItem(@Valid @RequestBody OpticalOrderItemSaveReqVO updateReqVO) {
        opticalOrderItemService.updateOpticalOrderItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除配镜订单明细")
    @PreAuthorize("@ss.hasPermission('opt:order:delete')")
    public CommonResult<Boolean> deleteOpticalOrderItem(@RequestParam("id") Long id) {
        opticalOrderItemService.deleteOpticalOrderItem(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除配镜订单明细")
    @PreAuthorize("@ss.hasPermission('opt:order:delete')")
    public CommonResult<Boolean> deleteOpticalOrderItemList(@RequestParam("ids") List<Long> ids) {
        opticalOrderItemService.deleteOpticalOrderItemList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得配镜订单明细")
    @PreAuthorize("@ss.hasPermission('opt:order:query')")
    public CommonResult<OpticalOrderItemRespVO> getOpticalOrderItem(@RequestParam("id") Long id) {
        OpticalOrderItemDO item = opticalOrderItemService.getOpticalOrderItem(id);
        return success(BeanUtils.toBean(item, OpticalOrderItemRespVO.class));
    }

    @GetMapping("/suggest")
    @Operation(summary = "获得历史项目名称建议")
    @PreAuthorize("@ss.hasPermission('opt:order:query')")
    public CommonResult<List<String>> getProductNameSuggestList(@RequestParam(value = "itemType", required = false) String itemType,
                                                                @RequestParam(value = "keyword", required = false) String keyword) {
        return success(opticalOrderItemService.getProductNameSuggestList(itemType, keyword));
    }

    @GetMapping("/page")
    @Operation(summary = "获得配镜订单明细分页")
    @PreAuthorize("@ss.hasPermission('opt:order:query')")
    public CommonResult<PageResult<OpticalOrderItemRespVO>> getOpticalOrderItemPage(@Valid OpticalOrderItemPageReqVO pageReqVO) {
        PageResult<OpticalOrderItemDO> pageResult = opticalOrderItemService.getOpticalOrderItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OpticalOrderItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出配镜订单明细 Excel")
    @PreAuthorize("@ss.hasPermission('opt:order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOpticalOrderItemExcel(@Valid OpticalOrderItemPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OpticalOrderItemDO> list = opticalOrderItemService.getOpticalOrderItemPage(pageReqVO).getList();
        ExcelUtils.write(response, "配镜订单明细.xls", "数据", OpticalOrderItemRespVO.class, BeanUtils.toBean(list, OpticalOrderItemRespVO.class));
    }

}