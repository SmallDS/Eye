package cn.iocoder.yudao.module.optical.controller.admin.importdata.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptImportOptometryExcelVO {

    @ExcelProperty("\u5ba2\u6237\u7f16\u53f7")
    private Long customerId;

    @ExcelProperty("客户手机号")
    private String customerMobile;

    @ExcelProperty("客户姓名")
    private String customerName;

    @ExcelProperty("验光时间")
    private LocalDateTime optometryTime;


    @ExcelProperty("远用右眼-球光")
    private String distanceRightSph;
    @ExcelProperty("远用右眼-散光")
    private String distanceRightCyl;
    @ExcelProperty("远用右眼-轴线")
    private String distanceRightAxis;
    @ExcelProperty("远用右眼-三棱")
    private String distanceRightPrism;
    @ExcelProperty("远用右眼-基底")
    private String distanceRightBase;
    @ExcelProperty("远用右眼-加光")
    private String distanceRightAddition;
    @ExcelProperty("远用右眼-基弧V")
    private String distanceRightBaseCurveV;
    @ExcelProperty("远用右眼-基弧H")
    private String distanceRightBaseCurveH;
    @ExcelProperty("远用右眼-直径")
    private String distanceRightDiameter;
    @ExcelProperty("远用右眼-裸眼视力")
    private String distanceRightNakedVision;
    @ExcelProperty("远用右眼-矫正视力")
    private String distanceRightCorrectedVision;

    @ExcelProperty("远用左眼-球光")
    private String distanceLeftSph;
    @ExcelProperty("远用左眼-散光")
    private String distanceLeftCyl;
    @ExcelProperty("远用左眼-轴线")
    private String distanceLeftAxis;
    @ExcelProperty("远用左眼-三棱")
    private String distanceLeftPrism;
    @ExcelProperty("远用左眼-基底")
    private String distanceLeftBase;
    @ExcelProperty("远用左眼-加光")
    private String distanceLeftAddition;
    @ExcelProperty("远用左眼-基弧V")
    private String distanceLeftBaseCurveV;
    @ExcelProperty("远用左眼-基弧H")
    private String distanceLeftBaseCurveH;
    @ExcelProperty("远用左眼-直径")
    private String distanceLeftDiameter;
    @ExcelProperty("远用左眼-裸眼视力")
    private String distanceLeftNakedVision;
    @ExcelProperty("远用左眼-矫正视力")
    private String distanceLeftCorrectedVision;

    @ExcelProperty("近用右眼-球光")
    private String nearRightSph;
    @ExcelProperty("近用右眼-散光")
    private String nearRightCyl;
    @ExcelProperty("近用右眼-轴线")
    private String nearRightAxis;
    @ExcelProperty("近用右眼-三棱")
    private String nearRightPrism;
    @ExcelProperty("近用右眼-基底")
    private String nearRightBase;
    @ExcelProperty("近用右眼-加光")
    private String nearRightAddition;
    @ExcelProperty("近用右眼-基弧V")
    private String nearRightBaseCurveV;
    @ExcelProperty("近用右眼-基弧H")
    private String nearRightBaseCurveH;
    @ExcelProperty("近用右眼-直径")
    private String nearRightDiameter;
    @ExcelProperty("近用右眼-裸眼视力")
    private String nearRightNakedVision;
    @ExcelProperty("近用右眼-矫正视力")
    private String nearRightCorrectedVision;

    @ExcelProperty("近用左眼-球光")
    private String nearLeftSph;
    @ExcelProperty("近用左眼-散光")
    private String nearLeftCyl;
    @ExcelProperty("近用左眼-轴线")
    private String nearLeftAxis;
    @ExcelProperty("近用左眼-三棱")
    private String nearLeftPrism;
    @ExcelProperty("近用左眼-基底")
    private String nearLeftBase;
    @ExcelProperty("近用左眼-加光")
    private String nearLeftAddition;
    @ExcelProperty("近用左眼-基弧V")
    private String nearLeftBaseCurveV;
    @ExcelProperty("近用左眼-基弧H")
    private String nearLeftBaseCurveH;
    @ExcelProperty("近用左眼-直径")
    private String nearLeftDiameter;
    @ExcelProperty("近用左眼-裸眼视力")
    private String nearLeftNakedVision;
    @ExcelProperty("近用左眼-矫正视力")
    private String nearLeftCorrectedVision;

    @ExcelProperty("远瞳距")
    private String distancePd;
    @ExcelProperty("RPD")
    private String rpd;
    @ExcelProperty("LPD")
    private String lpd;
    @ExcelProperty("近瞳距")
    private String nearPd;
    @ExcelProperty("Rh")
    private String rh;
    @ExcelProperty("Lh")
    private String lh;

    @ExcelProperty("备注")
    private String remark;

}
