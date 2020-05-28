package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.vo.RegionVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.entity.Region;
import org.linlinjava.litemall.db.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/region")
@Validated
public class AdminRegionController {
    private final Log logger = LogFactory.getLog(AdminRegionController.class);

    @Autowired
    private IRegionService regionService;

    @GetMapping("/clist")
    public Object clist(@NotNull Integer id) {
        List<Region> regionList = regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getPid, id));
        return ResponseUtil.okList(regionList);
    }

    @GetMapping("/list")
	@Cacheable(value = "regionCache" ,key = "'region:'+#root.method.name",condition = "true")
    public Object list() {
        List<RegionVo> regionVoList = new ArrayList<>();

        List<Region> litemallRegions = regionService.list();
        Map<Integer, List<Region>> collect = litemallRegions.stream().collect(Collectors.groupingBy(Region::getType));
        Integer provinceType = 1;
        List<Region> provinceList = collect.get(provinceType);
		Integer cityType = 2;
        List<Region> city = collect.get(cityType);
        Map<Integer, List<Region>> cityListMap = city.stream().collect(Collectors.groupingBy(Region::getPid));
		Integer areaType = 3;
        List<Region> areas = collect.get(areaType);
        Map<Integer, List<Region>> areaListMap = areas.stream().collect(Collectors.groupingBy(Region::getPid));

        for (Region province : provinceList) {
            RegionVo provinceVO = new RegionVo();
            provinceVO.setId(province.getId());
            provinceVO.setName(province.getName());
            provinceVO.setCode(province.getCode());
            provinceVO.setType(province.getType());

            List<Region> cityList = cityListMap.get(province.getId());
            List<RegionVo> cityVOList = new ArrayList<>();
            for (Region cityVo : cityList) {
                RegionVo cityVO = new RegionVo();
                cityVO.setId(cityVo.getId());
                cityVO.setName(cityVo.getName());
                cityVO.setCode(cityVo.getCode());
                cityVO.setType(cityVo.getType());

                List<Region> areaList = areaListMap.get(cityVo.getId());
                List<RegionVo> areaVOList = new ArrayList<>();
                for (Region area : areaList) {
                    RegionVo areaVO = new RegionVo();
                    areaVO.setId(area.getId());
                    areaVO.setName(area.getName());
                    areaVO.setCode(area.getCode());
                    areaVO.setType(area.getType());
                    areaVOList.add(areaVO);
                }

                cityVO.setChildren(areaVOList);
                cityVOList.add(cityVO);
            }
            provinceVO.setChildren(cityVOList);
            regionVoList.add(provinceVO);
        }

        return ResponseUtil.okList(regionVoList);
    }
}
