package cn.iocoder.yudao.module.optical.controller.admin.optometry.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 验光记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OptometryRecordRespVO {

    @ExcelProperty("编号")
    private Long id;
    @ExcelProperty("客户编号")
    private Long customerId;
    @ExcelProperty("验光时间")
    private LocalDateTime optometryTime;
    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("远用右眼球光")
    private BigDecimal distanceRightSph;
    @ExcelProperty("远用右眼散光")
    private BigDecimal distanceRightCyl;
    @ExcelProperty("远用右眼轴线")
    private BigDecimal distanceRightAxis;
    private BigDecimal distanceRightPrism;
    private String distanceRightBase;
    private BigDecimal distanceRightAddition;
    private BigDecimal distanceRightBaseCurveV;
    private BigDecimal distanceRightBaseCurveH;
    private BigDecimal distanceRightDiameter;
    private BigDecimal distanceRightNakedVision;
    private BigDecimal distanceRightCorrectedVision;

    @ExcelProperty("远用左眼球光")
    private BigDecimal distanceLeftSph;
    @ExcelProperty("远用左眼散光")
    private BigDecimal distanceLeftCyl;
    @ExcelProperty("远用左眼轴线")
    private BigDecimal distanceLeftAxis;
    private BigDecimal distanceLeftPrism;
    private String distanceLeftBase;
    private BigDecimal distanceLeftAddition;
    private BigDecimal distanceLeftBaseCurveV;
    private BigDecimal distanceLeftBaseCurveH;
    private BigDecimal distanceLeftDiameter;
    private BigDecimal distanceLeftNakedVision;
    private BigDecimal distanceLeftCorrectedVision;

    private BigDecimal nearRightSph;
    private BigDecimal nearRightCyl;
    private BigDecimal nearRightAxis;
    private BigDecimal nearRightPrism;
    private String nearRightBase;
    private BigDecimal nearRightAddition;
    private BigDecimal nearRightBaseCurveV;
    private BigDecimal nearRightBaseCurveH;
    private BigDecimal nearRightDiameter;
    private BigDecimal nearRightNakedVision;
    private BigDecimal nearRightCorrectedVision;

    private BigDecimal nearLeftSph;
    private BigDecimal nearLeftCyl;
    private BigDecimal nearLeftAxis;
    private BigDecimal nearLeftPrism;
    private String nearLeftBase;
    private BigDecimal nearLeftAddition;
    private BigDecimal nearLeftBaseCurveV;
    private BigDecimal nearLeftBaseCurveH;
    private BigDecimal nearLeftDiameter;
    private BigDecimal nearLeftNakedVision;
    private BigDecimal nearLeftCorrectedVision;

    @ExcelProperty("远瞳距")
    private BigDecimal distancePd;
    private BigDecimal rpd;
    private BigDecimal lpd;
    private BigDecimal nearPd;
    private BigDecimal rh;
    private BigDecimal lh;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
