package cn.iocoder.yudao.module.optical.dal.dataobject.customer;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("opt_customer")
@KeySequence("opt_customer_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDO extends TenantBaseDO {

    @TableId
    private Long id;
    private String name;
    private String mobile;
    private Integer gender;
    private Integer age;
    private String remark;
    private LocalDateTime latestOptometryTime;

}