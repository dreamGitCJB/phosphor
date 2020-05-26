/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package org.linlinjava.litemall.db.common.util;


import org.linlinjava.litemall.db.common.constans.StringPool;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集工具类
 *
 * @author L.cm
 */
public class Charsets {

	/**
	 * 字符集ISO-8859-1
	 */
	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
	public static final String ISO_8859_1_NAME = ISO_8859_1.name();

	/**
	 * 字符集GBK
	 */
	public static final Charset GBK = Charset.forName(StringPool.GBK);
	public static final String GBK_NAME = GBK.name();

	/**
	 * 字符集utf-8
	 */
	public static final Charset UTF_8 = StandardCharsets.UTF_8;
	public static final String UTF_8_NAME = UTF_8.name();

	/**
	 * 转换为Charset对象
	 *
	 * @param charsetName 字符集，为空则返回默认字符集
	 * @return Charsets
	 * @throws UnsupportedCharsetException 编码不支持
	 */
	public static Charset charset(String charsetName) throws UnsupportedCharsetException {
		return StringUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
	}

}
