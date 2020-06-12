package org.linlinjava.litemall.db.common.exception;

import lombok.Getter;
import org.linlinjava.litemall.db.common.result.IResultCode;
import org.linlinjava.litemall.db.common.result.ResultCode;

/**
 * @ClassName : ServiceException
 * @Description : 异常
 * @Author : chenjinbao
 * @Date : 2020/6/8 9:31 下午
 * @Version 1.0.0
 */

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 2359767895161832954L;

	@Getter
	private final IResultCode resultCode;

	public ServiceException(String message) {
		super(message);
		this.resultCode = ResultCode.FAILURE;
	}

	public ServiceException(IResultCode resultCode) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
	}

	public ServiceException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}

	/**
	 * 提高性能
	 *
	 * @return Throwable
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

	public Throwable doFillInStackTrace() {
		return super.fillInStackTrace();
	}
}
