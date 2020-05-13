package org.linlinjava.litemall.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.Region;
import org.linlinjava.litemall.db.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhy
 * @date 2019-01-17 23:07
 **/
@Component
public class GetRegionService {

	@Autowired
	private IRegionService regionService;

	private static List<Region> regions;

	protected List<Region> getLitemallRegions() {
		if(regions==null){
			createRegion();
		}
		return regions;
	}

	private synchronized void createRegion(){
		if (regions == null) {
			regions = regionService.list(new LambdaQueryWrapper<Region>().ne(Region::getType, 4));
		}
	}
}
