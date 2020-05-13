package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Comment;
import org.linlinjava.litemall.db.entity.Issue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 常见问题表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IIssueService extends IService<Issue> {

    IPage<Issue> querySelective(String question, Integer page, Integer limit, String sort, String order);


}
