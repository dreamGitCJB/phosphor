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
 * 商品货品表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "litemall_goods_product")
public class GoodsProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 商品表的商品ID
     */
    private Integer goodsId;

    /**
     * 商品规格值列表，采用JSON数组格式
     */
    @TableField(typeHandler = JsonStringArrayTypeHandler.class)
    private String[] specifications;

    /**
     * 商品货品价格
     */
    private BigDecimal price;

    /**
     * 商品货品数量
     */
    private Integer number;

    /**
     * 商品货品图片
     */
    private String url;




}
