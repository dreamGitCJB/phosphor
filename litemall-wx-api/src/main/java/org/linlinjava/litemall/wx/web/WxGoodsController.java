package org.linlinjava.litemall.wx.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.entity.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 商品服务
 */
@RestController
@RequestMapping("/wx/goods")
@Validated
public class WxGoodsController {
	private final Log logger = LogFactory.getLog(WxGoodsController.class);

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IGoodsProductService productService;

	@Autowired
	private IIssueService goodsIssueService;

	@Autowired
	private IGoodsAttributeService goodsAttributeService;

	@Autowired
	private IBrandService brandService;

	@Autowired
	private ICommentService commentService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICollectService collectService;

	@Autowired
	private IFootprintService footprintService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private ISearchHistoryService searchHistoryService;

	@Autowired
	private IGoodsSpecificationService goodsSpecificationService;

	@Autowired
	private IGrouponRulesService rulesService;

	private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);

	private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

	private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(16, 16, 1000, TimeUnit.MILLISECONDS, WORK_QUEUE, HANDLER);

	/**
	 * 商品详情
	 * <p>
	 * 用户可以不登录。
	 * 如果用户登录，则记录用户足迹以及返回用户收藏信息。
	 *
	 * @param userId 用户ID
	 * @param id     商品ID
	 * @return 商品详情
	 */
	@GetMapping("detail")
	public Object detail(@LoginUser Integer userId, @NotNull Integer id) {
		// 商品信息
		Goods info = goodsService.getById(id);

		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		RequestContextHolder.setRequestAttributes(sra, true);

		// 商品属性
		Callable<List> goodsAttributeListCallable = () -> goodsAttributeService.list(new LambdaQueryWrapper<GoodsAttribute>().eq(GoodsAttribute::getGoodsId, id));

		// 商品规格 返回的是定制的GoodsSpecificationVo
		Callable<Object> objectCallable = () -> goodsSpecificationService.getSpecificationVoList(id);

		// 商品规格对应的数量和价格
		Callable<List> productListCallable = () -> productService.queryByGid(id);

		// 商品问题，这里是一些通用问题
		Callable<List> issueCallable = () -> goodsIssueService.querySelective("", 1, 4, "", "").getRecords();

		// 商品品牌商
		Callable<Brand> brandCallable = ()->{
			Integer brandId = info.getBrandId();
			Brand brand;
			if (brandId == 0) {
				brand = new Brand();
			} else {
				brand = brandService.getById(info.getBrandId());
			}
			return brand;
		};

		// 评论
		Callable<Map> commentsCallable = () -> {
			IPage<Comment> commentIPage = commentService.queryGoodsByGid(id, 0, 2);
			List<Map<String, Object>> commentsVo = new ArrayList<>(commentIPage.getRecords().size());
			for (Comment comment : commentIPage.getRecords()) {
				Map<String, Object> c = new HashMap<>();
				c.put("id", comment.getId());
				c.put("addTime", comment.getAddTime());
				c.put("content", comment.getContent());
				c.put("adminContent", comment.getAdminContent());
				User user = userService.getById(comment.getUserId());
				c.put("nickname", user == null ? "" : user.getNickname());
				c.put("avatar", user == null ? "" : user.getAvatar());
				c.put("picList", comment.getPicUrls());
				commentsVo.add(c);
			}
			Map<String, Object> commentList = new HashMap<>();
			commentList.put("count", commentIPage.getTotal());
			commentList.put("data", commentsVo);
			return commentList;
		};

		//团购信息
		Callable<List> grouponRulesCallable = () ->rulesService.list(new LambdaQueryWrapper<GrouponRules>().eq(GrouponRules::getGoodsId, id));

		// 用户收藏
		int userHasCollect = 0;
		if (userId != null) {
			userHasCollect = collectService.count(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, userId)
					.eq(Collect::getValueId, id));
		}

		// 记录用户的足迹 异步处理
		if (userId != null) {
			executorService.execute(()->{
				Footprint footprint = new Footprint();
				footprint.setUserId(userId);
				footprint.setGoodsId(id);
				footprintService.save(footprint);
			});
		}
		FutureTask<List> goodsAttributeListTask = new FutureTask<>(goodsAttributeListCallable);
		FutureTask<Object> objectCallableTask = new FutureTask<>(objectCallable);
		FutureTask<List> productListCallableTask = new FutureTask<>(productListCallable);
		FutureTask<List> issueCallableTask = new FutureTask<>(issueCallable);
		FutureTask<Map> commentsCallableTsk = new FutureTask<>(commentsCallable);
		FutureTask<Brand> brandCallableTask = new FutureTask<>(brandCallable);
        FutureTask<List> grouponRulesCallableTask = new FutureTask<>(grouponRulesCallable);

		executorService.submit(goodsAttributeListTask);
		executorService.submit(objectCallableTask);
		executorService.submit(productListCallableTask);
		executorService.submit(issueCallableTask);
		executorService.submit(commentsCallableTsk);
		executorService.submit(brandCallableTask);
		executorService.submit(grouponRulesCallableTask);

		Map<String, Object> data = new HashMap<>();

		try {
			data.put("info", info);
			data.put("userHasCollect", userHasCollect);
			data.put("issue", issueCallableTask.get());
			data.put("comment", commentsCallableTsk.get());
			data.put("specificationList", objectCallableTask.get());
			data.put("productList", productListCallableTask.get());
			data.put("attribute", goodsAttributeListTask.get());
			data.put("brand", brandCallableTask.get());
			data.put("groupon", grouponRulesCallableTask.get());
			//SystemConfig.isAutoCreateShareImage()
			data.put("share", SystemConfig.isAutoCreateShareImage());

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//商品分享图片地址
		data.put("shareImage", info.getShareUrl());
		return ResponseUtil.ok(data);
	}

	/**
	 * 商品分类类目
	 *
	 * @param id 分类类目ID
	 * @return 商品分类类目
	 */
	@GetMapping("category")
	public Object category(@NotNull Integer id) {
		Category cur = categoryService.getById(id);
		Category parent = null;
		List<Category> children = null;

		if (cur.getPid() == 0) {
			parent = cur;
			children = categoryService.queryByPid(cur.getId());
			cur = children.size() > 0 ? children.get(0) : cur;
		} else {
			parent = categoryService.getById(cur.getPid());
			children = categoryService.queryByPid(cur.getPid());
		}
		Map<String, Object> data = new HashMap<>();
		data.put("currentCategory", cur);
		data.put("parentCategory", parent);
		data.put("brotherCategory", children);
		return ResponseUtil.ok(data);
	}

	/**
	 * 根据条件搜素商品
	 * <p>
	 * 1. 这里的前五个参数都是可选的，甚至都是空
	 * 2. 用户是可选登录，如果登录，则记录用户的搜索关键字
	 *
	 * @param categoryId 分类类目ID，可选
	 * @param brandId    品牌商ID，可选
	 * @param keyword    关键字，可选
	 * @param isNew      是否新品，可选
	 * @param isHot      是否热买，可选
	 * @param userId     用户ID
	 * @param page       分页页数
	 * @param limit       分页大小
	 * @param sort       排序方式，支持"add_time", "retail_price"或"name"
	 * @param order      排序类型，顺序或者降序
	 * @return 根据条件搜素的商品详情
	 */
	@GetMapping("list")
	public Object list(
		Integer categoryId,
		Integer brandId,
		String keyword,
		Boolean isNew,
		Boolean isHot,
		@LoginUser Integer userId,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "10") Integer limit,
		@Sort(accepts = {"add_time", "retail_price", "name"}) @RequestParam(defaultValue = "add_time") String sort,
		@Order @RequestParam(defaultValue = "desc") String order) {

		//添加到搜索历史
		if (userId != null && !StringUtils.isNullOrEmpty(keyword)) {
			SearchHistory searchHistoryVo = new SearchHistory();
			searchHistoryVo.setKeyword(keyword);
			searchHistoryVo.setUserId(userId);
			searchHistoryVo.setFrom("wx");
			searchHistoryService.save(searchHistoryVo);
		}

		//查询列表数据
		IPage<Goods> goodsIPage = goodsService.querySelective(categoryId, brandId, keyword, isHot, isNew, page, limit, sort, order);

		// 查询商品所属类目列表。
		List<Integer> goodsCatIds = goodsService.list(new LambdaQueryWrapper<Goods>()
				.eq(!org.springframework.util.StringUtils.isEmpty(brandId), Goods::getBrandId, brandId)
				.eq(!org.springframework.util.StringUtils.isEmpty(isNew), Goods::getIsNew, isNew)
				.eq(!org.springframework.util.StringUtils.isEmpty(isHot), Goods::getIsHot, isHot)
				.like(!org.springframework.util.StringUtils.isEmpty(keyword), Goods::getKeywords, keyword).or().like(!org.springframework.util.StringUtils.isEmpty(keyword), Goods::getName, keyword)
				.eq(Goods::getIsOnSale, true)).stream().map(Goods::getCategoryId).collect(Collectors.toList());
		List<Category> categoryList = null;
		if (goodsCatIds.size() != 0) {
			categoryList = categoryService.queryL2ByIds(goodsCatIds);
		} else {
			categoryList = new ArrayList<>(0);
		}


		Map<String, Object> entity = new HashMap<>();
		entity.put("list", goodsIPage.getRecords());
		entity.put("total", goodsIPage.getTotal());
		entity.put("page", goodsIPage.getCurrent());
		entity.put("limit", goodsIPage.getSize());
		entity.put("pages", goodsIPage.getPages());
		entity.put("filterCategoryList", categoryList);

		// 因为这里需要返回额外的filterCategoryList参数，因此不能方便使用ResponseUtil.okList
		return ResponseUtil.ok(entity);
	}

	/**
	 * 商品详情页面“大家都在看”推荐商品
	 *
	 * @param id, 商品ID
	 * @return 商品详情页面推荐商品
	 */
	@GetMapping("related")
	public Object related(@NotNull Integer id) {
		Goods goods = goodsService.getById(id);
		if (goods == null) {
			return ResponseUtil.badArgumentValue();
		}

		// 目前的商品推荐算法仅仅是推荐同类目的其他商品
		int cid = goods.getCategoryId();

		// 查找六个相关商品
		int related = 6;
		IPage<Goods> goodsList = goodsService.queryByCategory(cid, 0, related);
		return ResponseUtil.okPageList(goodsList);
	}

	/**
	 * 在售的商品总数
	 *
	 * @return 在售的商品总数
	 */
	@GetMapping("count")
	public Object count() {
		Integer goodsCount = goodsService.count(new LambdaQueryWrapper<Goods>().eq(Goods::getIsOnSale, true));
		return ResponseUtil.ok(goodsCount);
	}

}
