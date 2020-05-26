package org.linlinjava.litemall.db.common.config.rest;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : WxMappingJackson2HttpMessageConverter
 * @Description :
 * @Author : chenjinbao
 * @Date : 2020/5/4 10:46 PM
 * @Version 1.0.0
 */

public class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	public WxMappingJackson2HttpMessageConverter(){
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.TEXT_PLAIN);
		setSupportedMediaTypes(mediaTypes);// tag6
	}

}
