package org.linlinjava.litemall.wx.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.entity.Topic;
import org.linlinjava.litemall.db.service.IGoodsService;
import org.linlinjava.litemall.db.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 专题服务
 */
@RestController
@RequestMapping("/wx/topic")
@Validated
public class WxTopicController {
    private final Log logger = LogFactory.getLog(WxTopicController.class);

    @Autowired
    private ITopicService topicService;
    @Autowired
    private IGoodsService goodsService;

    /**
     * 专题列表
     *
     * @param page 分页页数
     * @param limit 分页大小
     * @return 专题列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        IPage<Topic> topicList = topicService.queryList(page, limit, sort, order);
        return ResponseUtil.okPageList(topicList);
    }

    /**
     * 专题详情
     *
     * @param id 专题ID
     * @return 专题详情
     */
    @GetMapping("detail")
    public Object detail(@NotNull Integer id) {
        Topic topic = topicService.getById(id);
        List<Goods> goods = new ArrayList<>();
        if (topic.getGoods().length > 0) {
            List<Integer> integers = Arrays.asList(topic.getGoods());
            integers.stream().forEach(o-> {
                Goods good = goodsService.getOne(new LambdaQueryWrapper<Goods>().eq(Goods::getId,o).eq(Goods::getIsOnSale, true));
                if (null != good) {
                    goods.add(good);
                }
            });
        }

        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("topic", topic);
        entity.put("goods", goods);
        return ResponseUtil.ok(entity);
    }

    /**
     * 相关专题
     *
     * @param id 专题ID
     * @return 相关专题
     */
    @GetMapping("related")
    public Object related(@NotNull Integer id) {
        IPage<Topic> topicRelatedList = topicService.queryRelatedList(id, 0, 4);
        return ResponseUtil.okPageList(topicRelatedList);
    }
}
