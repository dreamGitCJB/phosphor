package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.Category;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.mapper.CategoryMapper;
import org.linlinjava.litemall.db.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Override
    public List<Category> queryByPid(Integer pid) {
        return this.list(new LambdaQueryWrapper<Category>().eq(Category::getPid, pid));
    }

    @Override
    public List<Category> queryL1() {
        return this.list(new LambdaQueryWrapper<Category>().eq(Category::getLevel,"L1"));
    }

    @Override
    public List<Category> queryL2ByIds(List<Integer> pids) {

        List<Category> categories = this.list(new LambdaQueryWrapper<Category>().in(Category::getId, pids).eq(Category::getLevel, "L2"));
        return categories;
    }
}
