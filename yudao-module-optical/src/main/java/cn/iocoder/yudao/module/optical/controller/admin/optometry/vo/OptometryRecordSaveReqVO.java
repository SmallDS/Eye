package cn.iocoder.yudao.module.optical.controller.admin.optometry.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 验光记录新增/修改 Request VO")
@Data
public class OptometryRecordSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "客户不能为空")
    private Long customerId;

    @Schema(description = "验光时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "验光时间不能为空")
    private LocalDateTime optometryTime;


    @Schema(description = "备注")
    private String remark;

    private BigDecimal distanceRightSph;
    private BigDecimal distanceRightCyl;
    private BigDecimal distanceRightAxis;
    private BigDecimal distanceRightPrism;
    private String distanceRightBase;
    private BigDecimal distanceRightAddition;
    private BigDecimal distanceRightBaseCurveV;
    private BigDecimal distanceRightBaseCurveH;
    private BigDecimal distanceRightDiameter;
    private BigDecimal distanceRightNakedVision;
    private BigDecimal distanceRightCorrectedVision;

    private BigDecimal distanceLeftSph;
    private BigDecimal distanceLeftCyl;
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

    private BigDecimal distancePd;
    private BigDecimal rpd;
    private BigDecimal lpd;
    private BigDecimal nearPd;
    private BigDecimal rh;
    private BigDecimal lh;

}
