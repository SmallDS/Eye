package cn.iocoder.yudao.module.optical.controller.admin.importdata;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.excel.core.handler.ColumnWidthMatchStyleStrategy;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportCustomerExcelVO;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportOptometryExcelVO;
import cn.iocoder.yudao.module.optical.controller.admin.importdata.vo.OptImportRespVO;
import cn.iocoder.yudao.module.optical.service.importdata.OptImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.IMPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 东方镜通数据导入")
@RestController
@RequestMapping("/opt/import")
@Validated
public class OptImportController {

    @Resource
    private OptImportService optImportService;

    @GetMapping("/get-template")
    @Operation(summary = "获得东方镜通客户与验光导入模板")
    @PreAuthorize("@ss.hasPermission('opt:import:template')")
    @ApiAccessLog(operateType = EXPORT)
    public void getImportTemplate(HttpServletResponse response) throws IOException {
        List<OptImportCustomerExcelVO> customers = List.of(
                OptImportCustomerExcelVO.builder().id(1L).name("张三").mobile("13800138000").gender("男").age(32).remark("示例客户").build()
        );
        List<OptImportOptometryExcelVO> optometryRecords = List.of(
                OptImportOptometryExcelVO.builder().customerId(1L).customerMobile("13800138000").customerName("张三").optometryTime(java.time.LocalDateTime.now()).distanceRightSph("-1.25")
                        .distanceLeftSph("-1.00").distancePd("62")
                        .remark("示例验光单；验光时间请填写 yyyy-MM-dd HH:mm:ss").build()
        );
        ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream())
                .autoCloseStream(false)
                .registerWriteHandler(new ColumnWidthMatchStyleStrategy())
                .build();
        try {
            WriteSheet customerSheet = FastExcelFactory.writerSheet(0, "客户")
                    .head(OptImportCustomerExcelVO.class).build();
            WriteSheet optometrySheet = FastExcelFactory.writerSheet(1, "验光单")
                    .head(OptImportOptometryExcelVO.class).build();
            excelWriter.write(customers, customerSheet);
            excelWriter.write(optometryRecords, optometrySheet);
        } finally {
            excelWriter.finish();
        }
        response.addHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8("东方镜通导入模板.xls"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    @PostMapping("/excel")
    @Operation(summary = "导入东方镜通客户与验光数据")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @PreAuthorize("@ss.hasPermission('opt:import:create')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<OptImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                     @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        byte[] bytes = file.getBytes();
        List<OptImportCustomerExcelVO> customers = readSheet(bytes, "客户", OptImportCustomerExcelVO.class);
        List<OptImportOptometryExcelVO> optometryRecords = readSheet(bytes, "验光单", OptImportOptometryExcelVO.class);
        return success(optImportService.importData(customers, optometryRecords, Boolean.TRUE.equals(updateSupport)));
    }

    private <T> List<T> readSheet(byte[] bytes, String sheetName, Class<T> head) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            return FastExcelFactory.read(inputStream, head, null)
                    .autoCloseStream(false)
                    .sheet(sheetName)
                    .doReadSync();
        } catch (RuntimeException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains(sheetName)) {
                return List.of();
            }
            throw ex;
        }
    }

}