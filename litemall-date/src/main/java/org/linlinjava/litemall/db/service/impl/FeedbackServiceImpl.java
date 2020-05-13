package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.entity.Feedback;
import org.linlinjava.litemall.db.mapper.FeedbackMapper;
import org.linlinjava.litemall.db.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
