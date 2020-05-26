package org.linlinjava.litemall.db.common.config;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * @ClassName : ContextAwareCallable
 * @Description :
 * @Author : chenjinbao
 * @Date : 2020/5/26 5:25 下午
 * @Version 1.0.0
 */

public class ContextAwareCallable<T> implements Callable<T> {
	private Callable<T> task;
	private RequestAttributes context;

	public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
		this.task = task;
		this.context = context;
	}

	@Override
	public T call() throws Exception {
		if (context != null) {
			RequestContextHolder.setRequestAttributes(context);
		}

		try {
			return task.call();
		} finally {
			RequestContextHolder.resetRequestAttributes();
		}
	}
}
