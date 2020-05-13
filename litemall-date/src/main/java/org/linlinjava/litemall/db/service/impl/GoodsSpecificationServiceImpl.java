package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.GoodsSpecification;
import org.linlinjava.litemall.db.mapper.GoodsSpecificationMapper;
import org.linlinjava.litemall.db.service.IGoodsSpecificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品规格表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class GoodsSpecificationServiceImpl extends ServiceImpl<GoodsSpecificationMapper, GoodsSpecification> implements IGoodsSpecificationService {
    @Override
    public Object getSpecificationVoList(Integer goodsId) {
        List<GoodsSpecification> goodsSpecificationList = this.list(new LambdaQueryWrapper<GoodsSpecification>()
                .eq(GoodsSpecification::getGoodsId, goodsId));
        Map<String, VO> map = new HashMap<>();
        List<VO> specificationVoList = new ArrayList<>();

        for (GoodsSpecification goodsSpecification : goodsSpecificationList) {
            String specification = goodsSpecification.getSpecification();
            VO goodsSpecificationVo = map.get(specification);
            if (goodsSpecificationVo == null) {
                goodsSpecificationVo = new VO();
                goodsSpecificationVo.setName(specification);
                List<GoodsSpecification> valueList = new ArrayList<>();
                valueList.add(goodsSpecification);
                goodsSpecificationVo.setValueList(valueList);
                map.put(specification, goodsSpecificationVo);
                specificationVoList.add(goodsSpecificationVo);
            } else {
                List<GoodsSpecification> valueList = goodsSpecificationVo.getValueList();
                valueList.add(goodsSpecification);
            }
        }

        return specificationVoList;
    }

    private class VO {
        private String name;
        private List<GoodsSpecification> valueList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<GoodsSpecification> getValueList() {
            return valueList;
        }

        public void setValueList(List<GoodsSpecification> valueList) {
            this.valueList = valueList;
        }
    }
}
