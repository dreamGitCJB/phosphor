package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.entity.SearchHistory;
import org.linlinjava.litemall.db.mapper.SearchHistoryMapper;
import org.linlinjava.litemall.db.service.ISearchHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 搜索历史表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements ISearchHistoryService {

}
