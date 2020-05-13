package org.linlinjava.litemall.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信配置表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_wx_config")
public class WxConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 小程序名称
     */
    private String appName;

    /**
     * 小程序AppID
     */
    private String appId;

    /**
     * 小程序AppSecret
     */
    private String appSecret;

    /**
     * 客服按钮(0不显示 1显示)
     */
    private Integer isService;

    /**
     * 客服图标(关联文件记录表id)
     */
    private Integer serviceImageId;

    /**
     * 电话客服按钮(0不显示 1显示)
     */
    private Integer isPhone;

    /**
     * 电话号码
     */
    private String phoneNo;

    /**
     * 电话图标
     */
    private Integer phoneImageId;

    /**
     * 微信商户号id
     */
    private String mchid;

    /**
     * 微信支付密钥
     */
    private String apikey;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 更新时间
     */
    private Integer updateTime;


}
