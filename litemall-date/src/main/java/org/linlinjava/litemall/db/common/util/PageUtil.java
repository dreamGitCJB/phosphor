package org.linlinjava.litemall.db.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.constans.DbConstans;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName : PageUtil
 * @Description : page转化
 * @Author : chenjinbao
 * @Date : 2020/5/3 8:33 PM
 * @Version 1.0.0
 */

public class PageUtil<R,T> {

	public IPage pagetoPage(IPage<R> page, List<T> list) {
		IPage<T> iPage = new Page<>();
		iPage.setTotal(page.getTotal());
		iPage.setSize(page.getSize());
		iPage.setCurrent(page.getCurrent());
		iPage.setPages(page.getPages());
		iPage.setRecords(list);
		return iPage;
	}

	public static void pagetoPage(Page page, String sort, String order) {
		if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
			if (DbConstans.SORT_ASC.contains(order)) {
				page.setAsc(sort);
			} else {
				page.setDesc(sort);
			}
		} else {
			page.setDesc("add_time");
		}

	}

}
