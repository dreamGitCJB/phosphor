package org.linlinjava.litemall.core.wx.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.core.vo.AuthCode2SessionVo;
import org.linlinjava.litemall.core.wx.IWxService;
import org.linlinjava.litemall.db.common.constans.RedisKey;
import org.linlinjava.litemall.db.entity.WxConfig;
import org.linlinjava.litemall.db.service.IWxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName : WxServiceImpl
 * @Description : 微信实现
 * @Author : chenjinbao
 * @Date : 2020/5/4 5:36 PM
 * @Version 1.0.0
 */
@Service
public class WxServiceImpl implements IWxService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IWxConfigService wxConfigService;

	/**
	 * 登录凭证校验URL
	 */
	private static String AUTH_CODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

	/**
	 * 登录凭证校验
	 *
	 * @param code
	 * @return
	 */
	@Override
	public AuthCode2SessionVo authCode2Session(String code, String tenantId) {
		WxConfig wxConfig = this.wxConfig(tenantId);
		ResponseEntity<AuthCode2SessionVo> forEntity = restTemplate.getForEntity(String.format(AUTH_CODE_2_SESSION, wxConfig.getAppId(), wxConfig.getAppSecret(), code), AuthCode2SessionVo.class);

		return forEntity.getBody();
	}


	public WxConfig wxConfig(String tenantId) {

		WxConfig wxConfig = (WxConfig)redisTemplate.opsForHash().get(RedisKey.WX_CONFIG, tenantId);
		if (wxConfig == null) {
			wxConfig = wxConfigService.getOne(new LambdaQueryWrapper<WxConfig>().eq(WxConfig::getTenantId, tenantId));
			if (wxConfig == null) {
				throw new RuntimeException("请配置微信信息");
			}
		}
		return wxConfig;
	}
}
