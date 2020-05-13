package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Topic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 专题表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ITopicService extends IService<Topic> {

    IPage<Topic> queryList(int offset, int limit, String sort, String order);

    IPage<Topic> queryRelatedList(Integer id, int offset, int limit);
}
