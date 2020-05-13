package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Issue;
import org.linlinjava.litemall.db.mapper.IssueMapper;
import org.linlinjava.litemall.db.service.IIssueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 常见问题表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IIssueService {
    @Override
    public IPage<Issue> querySelective(String question, Integer current, Integer limit, String sort, String order) {
        Page page = new Page(current, limit);

        PageUtil.pagetoPage(page, sort, order);

        IPage<Issue> issueIPage = this.page(page, new LambdaQueryWrapper<Issue>().like(!StringUtils.isEmpty(question), Issue::getQuestion, question));

        return issueIPage;
    }
}
