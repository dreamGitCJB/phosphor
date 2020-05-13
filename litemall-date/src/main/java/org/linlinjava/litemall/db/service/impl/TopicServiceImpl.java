package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Topic;
import org.linlinjava.litemall.db.mapper.TopicMapper;
import org.linlinjava.litemall.db.service.ITopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 专题表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements ITopicService {
    @Override
    public IPage<Topic> queryList(int offset, int limit, String sort, String order) {
        Page page = new Page(offset, limit);
        PageUtil.pagetoPage(page, sort, order);

        IPage<Topic> topicIPage  = this.page(page);

        return topicIPage;
    }

    @Override
    public IPage<Topic> queryRelatedList(Integer id, int offset, int limit) {

        List<Topic> topics = this.list(new LambdaQueryWrapper<Topic>().eq(Topic::getId, id));

        if (topics.size() == 0) {
            return queryList(offset, limit, "add_time", "desc");
        }
        Topic topic = topics.get(0);
        Page page = new Page(offset, limit);

        IPage<Topic> relateds = this.page(page, new LambdaQueryWrapper<Topic>().ne(Topic::getId, topic.getId()));
        if (relateds.getRecords().size() != 0) {
            return relateds;
        }

        return queryList(offset, limit, "add_time", "desc");
    }
}
