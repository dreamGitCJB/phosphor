package org.linlinjava.litemall.db.common.config.batis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName : MyMetaObjectHandler
 * @Description : 自动插入
 * @Author : chenjinbao
 * @Date : 2020/5/25 3:01 下午
 * @Version 1.0.0
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, "add_time", LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
	}
}
