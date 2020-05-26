package org.linlinjava.litemall.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品参数表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_goods_attribute")
public class GoodsAttribute extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 商品表的商品ID
     */
    private Integer goodsId;

    /**
     * 商品参数名称
     */
    private String attribute;

    /**
     * 商品参数值
     */
    private String value;




}
