package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.entity.Log;
import org.linlinjava.litemall.db.mapper.LogMapper;
import org.linlinjava.litemall.db.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
