package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 类目表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ICategoryService extends IService<Category> {

    List<Category> queryByPid(Integer pid);

    List<Category> queryL1();

    List<Category> queryL2ByIds(List<Integer> pids);
}
