package cn.iocoder.yudao.module.optical.controller.admin.customer;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerRespVO;
import cn.iocoder.yudao.module.optical.controller.admin.customer.vo.CustomerSaveReqVO;
import cn.iocoder.yudao.module.optical.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.module.optical.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "管理后台 - 客户管理")
@RestController
@RequestMapping("/opt/customer")
@Validated
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping("/create")
    @Operation(summary = "创建客户")
    @PreAuthorize("@ss.hasPermission('opt:customer:create')")
    public CommonResult<Long> createCustomer(@Valid @RequestBody CustomerSaveReqVO createReqVO) {
        return success(customerService.createCustomer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户")
    @PreAuthorize("@ss.hasPermission('opt:customer:update')")
    public CommonResult<Boolean> updateCustomer(@Valid @RequestBody CustomerSaveReqVO updateReqVO) {
        customerService.updateCustomer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('opt:customer:delete')")
    public CommonResult<Boolean> deleteCustomer(@RequestParam("id") Long id) {
        customerService.deleteCustomer(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除客户")
    @PreAuthorize("@ss.hasPermission('opt:customer:delete')")
    public CommonResult<Boolean> deleteCustomerList(@RequestParam("ids") List<Long> ids) {
        customerService.deleteCustomerList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户")
    @PreAuthorize("@ss.hasPermission('opt:customer:query')")
    public CommonResult<CustomerRespVO> getCustomer(@RequestParam("id") Long id) {
        CustomerDO customer = customerService.getCustomer(id);
        return success(BeanUtils.toBean(customer, CustomerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户分页")
    @PreAuthorize("@ss.hasPermission('opt:customer:query')")
    public CommonResult<PageResult<CustomerRespVO>> getCustomerPage(@Valid CustomerPageReqVO pageReqVO) {
        PageResult<CustomerDO> pageResult = customerService.getCustomerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CustomerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户 Excel")
    @PreAuthorize("@ss.hasPermission('opt:customer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCustomerExcel(@Valid CustomerPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CustomerDO> list = customerService.getCustomerPage(pageReqVO).getList();
        ExcelUtils.write(response, "客户.xls", "数据", CustomerRespVO.class, BeanUtils.toBean(list, CustomerRespVO.class));
    }

}