package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linlinjava.litemall.db.entity.Comment;
import org.linlinjava.litemall.db.mapper.CommentMapper;
import org.linlinjava.litemall.db.service.ICommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Override
    public IPage<Comment> query(Integer type, Integer valueId, Integer showType, Integer offset, Integer limit) {

        Page page = new Page(offset, limit);

        return this.page(page, new LambdaQueryWrapper<Comment>().eq(Comment::getValueId, valueId)
                .eq(Comment::getType, type)
                .eq(showType == 1, Comment::getHasPicture, true));

    }

    @Override
    public IPage<Comment> queryGoodsByGid(Integer id, int offset, int limit) {

        Page page = new Page(offset, limit);

        return this.page(page, new LambdaQueryWrapper<Comment>().eq(Comment::getValueId, id)
                .eq(Comment::getType, 0));
    }
}
