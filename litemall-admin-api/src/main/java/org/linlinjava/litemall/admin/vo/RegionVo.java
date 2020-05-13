package org.linlinjava.litemall.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class RegionVo {
    private Integer id;
    private String name;
    private Integer type;
    private Integer code;

    private List<RegionVo> children;


}
