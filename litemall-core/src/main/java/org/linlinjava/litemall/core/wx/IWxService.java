package org.linlinjava.litemall.core.wx;

import org.linlinjava.litemall.core.vo.AuthCode2SessionVo;

/**
 * @ClassName : IWxService
 * @Description : 微信相关的操作
 * @Author : chenjinbao
 * @Date : 2020/5/4 5:34 PM
 * @Version 1.0.0
 */

public interface IWxService {

	/**
	 * 登录凭证校验
	 * @param code
	 * @return
	 */
	 AuthCode2SessionVo authCode2Session(String code, String tenantId);


}
