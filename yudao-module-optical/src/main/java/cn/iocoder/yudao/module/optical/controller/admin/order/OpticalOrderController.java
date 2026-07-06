package cn.iocoder.yudao.module.optical.controller.admin.order;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderRespVO;
import cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.order.OpticalOrderDO;
import cn.iocoder.yudao.module.optical.service.order.OpticalOrderItemService;
import cn.iocoder.yudao.module.optical.service.order.OpticalOrderService;
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

@Tag(name = "管理后台 - 配镜订单")
@RestController
@RequestMapping("/opt/order")
@Validated
public class OpticalOrderController {

    @Resource
    private OpticalOrderService opticalOrderService;
    @Resource
    private OpticalOrderItemService opticalOrderItemService;

    @PostMapping("/create")
    @Operation(summary = "创建配镜订单")
    @PreAuthorize("@ss.hasPermission('opt:order:create')")
    public CommonResult<Long> createOpticalOrder(@Valid @RequestBody OpticalOrderSaveReqVO createReqVO) {
        return success(opticalOrderService.createOpticalOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新配镜订单")
    @PreAuthorize("@ss.hasPermission('opt:order:update')")
    public CommonResult<Boolean> updateOpticalOrder(@Valid @RequestBody OpticalOrderSaveReqVO updateReqVO) {
        opticalOrderService.updateOpticalOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除配镜订单")
    @PreAuthorize("@ss.hasPermission('opt:order:delete')")
    public CommonResult<Boolean> deleteOpticalOrder(@RequestParam("id") Long id) {
        opticalOrderService.deleteOpticalOrder(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除配镜订单")
    @PreAuthorize("@ss.hasPermission('opt:order:delete')")
    public CommonResult<Boolean> deleteOpticalOrderList(@RequestParam("ids") List<Long> ids) {
        opticalOrderService.deleteOpticalOrderList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得配镜订单")
    @PreAuthorize("@ss.hasPermission('opt:order:query')")
    public CommonResult<OpticalOrderRespVO> getOpticalOrder(@RequestParam("id") Long id) {
        OpticalOrderDO order = opticalOrderService.getOpticalOrder(id);
        OpticalOrderRespVO respVO = BeanUtils.toBean(order, OpticalOrderRespVO.class);
        respVO.setItems(BeanUtils.toBean(opticalOrderItemService.getOpticalOrderItemListByOrderId(id), cn.iocoder.yudao.module.optical.controller.admin.order.vo.OpticalOrderItemRespVO.class));
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得配镜订单分页")
    @PreAuthorize("@ss.hasPermission('opt:order:query')")
    public CommonResult<PageResult<OpticalOrderRespVO>> getOpticalOrderPage(@Valid OpticalOrderPageReqVO pageReqVO) {
        PageResult<OpticalOrderDO> pageResult = opticalOrderService.getOpticalOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OpticalOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出配镜订单 Excel")
    @PreAuthorize("@ss.hasPermission('opt:order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOpticalOrderExcel(@Valid OpticalOrderPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OpticalOrderDO> list = opticalOrderService.getOpticalOrderPage(pageReqVO).getList();
        ExcelUtils.write(response, "配镜订单.xls", "数据", OpticalOrderRespVO.class, BeanUtils.toBean(list, OpticalOrderRespVO.class));
    }

}