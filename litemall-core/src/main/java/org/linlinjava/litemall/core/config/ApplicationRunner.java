package org.linlinjava.litemall.core.config;

import org.linlinjava.litemall.core.system.SystemInistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ApplicationRunner
 * @Description :
 * @Author : chenjinbao
 * @Date : 2020/5/26 10:43 上午
 * @Version 1.0.0
 */
@Component
public class ApplicationRunner implements CommandLineRunner {

	@Autowired
	SystemInistService systemInistService;
	@Override
	public void run(String... args) throws Exception {
		systemInistService.inist();
	}
}
