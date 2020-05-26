package org.linlinjava.litemall.db.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.linlinjava.litemall.db.common.handler.JsonStringArrayTypeHandler;

/**
 * <p>
 * 商品基本信息表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_goods")
public class Goods extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 商品编号
     */
    private String goodsSn;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品所属类目ID
     */
    private Integer categoryId;

    private Integer brandId;

    /**
     * 商品宣传图片列表，采用JSON数组格式
     */

    @TableField(typeHandler = JsonStringArrayTypeHandler.class)
    private String[] gallery;

    /**
     * 商品关键字，采用逗号间隔
     */
    private String keywords;

    /**
     * 商品简介
     */
    private String brief;

    /**
     * 是否上架
     */
    private Boolean isOnSale;

    private Integer sortOrder;

    /**
     * 商品页面商品图片
     */
    private String picUrl;

    /**
     * 商品分享朋友圈图片
     */
    private String shareUrl;

    /**
     * 是否新品首发，如果设置则可以在新品首发页面展示
     */
    private Boolean isNew;

    /**
     * 是否人气推荐，如果设置则可以在人气推荐页面展示
     */
    private Boolean isHot;

    /**
     * 商品单位，例如件、盒
     */
    private String unit;

    /**
     * 专柜价格
     */
    private BigDecimal counterPrice;

    /**
     * 零售价格
     */
    private BigDecimal retailPrice;

    /**
     * 商品详细介绍，是富文本格式
     */
    private String detail;




}
