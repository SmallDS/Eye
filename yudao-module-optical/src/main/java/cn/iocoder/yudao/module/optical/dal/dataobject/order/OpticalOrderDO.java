package cn.iocoder.yudao.module.optical.dal.dataobject.order;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("opt_order")
@KeySequence("opt_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpticalOrderDO extends TenantBaseDO {

    @TableId
    private Long id;
    private String orderNo;
    private Long customerId;
    private Long optometryRecordId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal receivableAmount;
    private BigDecimal paidAmount;
    private LocalDateTime orderTime;
    private LocalDateTime pickupTime;
    private String remark;

}
