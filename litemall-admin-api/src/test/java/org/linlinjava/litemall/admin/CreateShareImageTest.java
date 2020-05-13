package org.linlinjava.litemall.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.litemall.core.qcode.QCodeService;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateShareImageTest {
    @Autowired
    QCodeService qCodeService;
    @Autowired
    IGoodsService goodsService;

    @Test
    public void test() {
        Goods good = goodsService.getById(1181010);
        qCodeService.createGoodShareImage(good.getId().toString(), good.getPicUrl(), good.getName());
    }
}
