package org.linlinjava.litemall.wx.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.entity.Keyword;
import org.linlinjava.litemall.db.entity.SearchHistory;
import org.linlinjava.litemall.db.service.IKeywordService;
import org.linlinjava.litemall.db.service.ISearchHistoryService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品搜索服务
 * <p>
 * 注意：目前搜索功能非常简单，只是基于关键字匹配。
 */
@RestController
@RequestMapping("/wx/search")
@Validated
public class WxSearchController {
    private final Log logger = LogFactory.getLog(WxSearchController.class);

    @Autowired
    private IKeywordService keywordsService;
    @Autowired
    private ISearchHistoryService searchHistoryService;

    /**
     * 搜索页面信息
     * <p>
     * 如果用户已登录，则给出用户历史搜索记录；
     * 如果没有登录，则给出空历史搜索记录。
     *
     * @param userId 用户ID，可选
     * @return 搜索页面信息
     */
    @GetMapping("index")
    public Object index(@LoginUser Integer userId) {
        //取出输入框默认的关键词
        Keyword defaultKeyword = keywordsService.getOne(new LambdaQueryWrapper<Keyword>().eq(Keyword::getIsDefault, true));
        //取出热闹关键词
        List<Keyword> hotKeywordList = keywordsService.list(new LambdaQueryWrapper<Keyword>().eq(Keyword::getIsHot, true));

        List<SearchHistory> historyList = null;
        if (userId != null) {
            //取出用户历史关键字
            historyList = searchHistoryService.list(new LambdaQueryWrapper<SearchHistory>().eq(SearchHistory::getUserId, userId));
        } else {
            historyList = new ArrayList<>(0);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("defaultKeyword", defaultKeyword);
        data.put("historyKeywordList", historyList);
        data.put("hotKeywordList", hotKeywordList);
        return ResponseUtil.ok(data);
    }

    /**
     * 关键字提醒
     * <p>
     * 当用户输入关键字一部分时，可以推荐系统中合适的关键字。
     *
     * @param keyword 关键字
     * @return 合适的关键字
     */
    @GetMapping("helper")
    public Object helper(@NotEmpty String keyword) {

        List<Keyword> keywordsList = keywordsService.list(new LambdaQueryWrapper<Keyword>().like(Keyword::getKeyword, keyword));

        Set<String> collect = keywordsList.stream().map(Keyword::getKeyword).limit(10).collect(Collectors.toSet());

        return ResponseUtil.ok(collect);
    }

    /**
     * 清除用户搜索历史
     *
     * @param userId 用户ID
     * @return 清理是否成功
     */
    @PostMapping("clearhistory")
    public Object clearhistory(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        searchHistoryService.remove(new LambdaQueryWrapper<SearchHistory>().eq(SearchHistory::getUserId, userId));
        return ResponseUtil.ok();
    }
}
