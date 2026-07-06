package cn.iocoder.yudao.module.optical.dal.dataobject.optometry;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("opt_optometry_record")
@KeySequence("opt_optometry_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptometryRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long customerId;
    private LocalDateTime optometryTime;
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

    // 旧字段保留，兼容早期 opt_optometry_record 数据。
    private BigDecimal leftSph;
    private BigDecimal leftCyl;
    private BigDecimal leftAxis;
    private BigDecimal leftVision;
    private BigDecimal rightSph;
    private BigDecimal rightCyl;
    private BigDecimal rightAxis;
    private BigDecimal rightVision;
    private BigDecimal pupilDistance;
    private Long optometristUserId;

}
