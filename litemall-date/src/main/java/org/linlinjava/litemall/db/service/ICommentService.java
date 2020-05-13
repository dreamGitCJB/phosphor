package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ICommentService extends IService<Comment> {

    IPage<Comment> query(Integer type, Integer valueId, Integer showType, Integer offset, Integer limit);

    IPage<Comment> queryGoodsByGid(Integer id, int offset, int limit);
}
