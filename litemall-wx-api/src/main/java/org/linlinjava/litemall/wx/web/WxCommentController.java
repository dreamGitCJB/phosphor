package org.linlinjava.litemall.wx.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Comment;
import org.linlinjava.litemall.db.service.ICommentService;
import org.linlinjava.litemall.db.service.IGoodsService;
import org.linlinjava.litemall.db.service.ITopicService;
import org.linlinjava.litemall.db.service.IUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.dto.UserInfo;
import org.linlinjava.litemall.wx.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户评论服务
 */
@RestController
@RequestMapping("/wx/comment")
@Validated
public class WxCommentController {
    private final Log logger = LogFactory.getLog(WxCommentController.class);

    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ITopicService topicService;

    private Object validate(Comment comment) {
        String content = comment.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseUtil.badArgument();
        }

        Integer star = comment.getStar();
        if (star == null) {
            return ResponseUtil.badArgument();
        }
        if (star < 0 || star > 5) {
            return ResponseUtil.badArgumentValue();
        }

        Integer type = comment.getType();
        Integer valueId = comment.getValueId();
        if (type == null || valueId == null) {
            return ResponseUtil.badArgument();
        }
        if (type == 0) {
            if (goodsService.getById(valueId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        } else if (type == 1) {
            if (topicService.getById(valueId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        } else {
            return ResponseUtil.badArgumentValue();
        }
        Boolean hasPicture = comment.getHasPicture();
        if (hasPicture == null || !hasPicture) {
            comment.setPicUrls(JSON.toJSONString(new ArrayList<>()));
        }
        return null;
    }

    /**
     * 发表评论
     *
     * @param userId  用户ID
     * @param comment 评论内容
     * @return 发表评论操作结果
     */
    @PostMapping("post")
    public Object post(@LoginUser Integer userId, @RequestBody Comment comment) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Object error = validate(comment);
        if (error != null) {
            return error;
        }

        comment.setUserId(userId);
        commentService.save(comment);
        return ResponseUtil.ok(comment);
    }

    /**
     * 评论数量
     *
     * @param type    类型ID。 如果是0，则查询商品评论；如果是1，则查询专题评论。
     * @param valueId 商品或专题ID。如果type是0，则是商品ID；如果type是1，则是专题ID。
     * @return 评论数量
     */
    @GetMapping("count")
    public Object count(@NotNull Byte type, @NotNull Integer valueId) {
        int allCount = commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getType, type).eq(Comment::getValueId, valueId));
        int hasPicCount = commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getType, type).eq(Comment::getValueId, valueId).eq(Comment::getHasPicture, true));
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("allCount", allCount);
        entity.put("hasPicCount", hasPicCount);
        return ResponseUtil.ok(entity);
    }

    /**
     * 评论列表
     *
     * @param type     类型ID。 如果是0，则查询商品评论；如果是1，则查询专题评论。
     * @param valueId  商品或专题ID。如果type是0，则是商品ID；如果type是1，则是专题ID。
     * @param showType 显示类型。如果是0，则查询全部；如果是1，则查询有图片的评论。
     * @param page     分页页数
     * @param limit     分页大小
     * @return 评论列表
     */
    @GetMapping("list")
    public Object list(@NotNull Integer type,
                       @NotNull Integer valueId,
                       @NotNull Integer showType,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        IPage<Comment> commentIPage = commentService.query(type, valueId, showType, page, limit);

        List<Map<String, Object>> commentVoList = new ArrayList<>(commentIPage.getRecords().size());
        for (Comment comment : commentIPage.getRecords()) {
            Map<String, Object> commentVo = new HashMap<>();
            commentVo.put("addTime", comment.getAddTime());
            commentVo.put("content", comment.getContent());
            commentVo.put("adminContent", comment.getAdminContent());
            commentVo.put("picList", comment.getPicUrls());

            UserInfo userInfo = userInfoService.getInfo(comment.getUserId());
            commentVo.put("userInfo", userInfo);

            commentVoList.add(commentVo);
        }
        IPage iPage = new PageUtil().pagetoPage(commentIPage, commentVoList);
        return ResponseUtil.okPageList(iPage);
    }
}