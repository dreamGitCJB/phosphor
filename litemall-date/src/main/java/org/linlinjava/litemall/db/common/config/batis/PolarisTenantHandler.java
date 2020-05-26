package org.linlinjava.litemall.db.common.config.batis;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.linlinjava.litemall.db.common.constans.CommonConstant;
import org.linlinjava.litemall.db.common.constans.TokenConstant;
import org.linlinjava.litemall.db.common.util.AuthUtil;
import org.linlinjava.litemall.db.common.util.Func;
import org.linlinjava.litemall.db.common.util.StringUtil;
import org.linlinjava.litemall.db.common.util.WebUtil;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName : PolarisTenantHandler
 * @Description : mybatis-plus多租户配置
 * @Author : chenjinbao
 * @Date : 2020/5/25 9:07 下午
 * @Version 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class PolarisTenantHandler implements TenantHandler, SmartInitializingSingleton {

	private static String column = "tenant_id";
	/**
	 * 匹配的多租户表
	 */
	private final List<String> tenantTableList = new ArrayList<>();
	/**
	 * 需要排除进行自定义的多租户表
	 */
	private final List<String> excludeTableList = Arrays.asList("");


	/**
	 * 获取租户ID
	 *
	 * @return 租户ID
	 */
	@Override
	public Expression getTenantId(boolean where) {
		return new StringValue(Func.toStr(AuthUtil.getTenantId(), CommonConstant.DEFAULT_TENANT));
	}

	/**
	 * 获取租户字段名称
	 *
	 * @return 租户字段名称
	 */
	@Override
	public String getTenantIdColumn() {
		return column;
	}

	/**
	 * 过滤租户表
	 *
	 * @param tableName 表名
	 * @return 是否进行过滤
	 */
	@Override
	public boolean doTableFilter(String tableName) {
		return !(tenantTableList.contains(tableName) && StringUtil.isNotBlank(AuthUtil.getTenantId()));
	}

	@Override
	public void afterSingletonsInstantiated() {
		List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
		tableFor:
		for (TableInfo tableInfo : tableInfos) {
			String tableName = tableInfo.getTableName();
			if (excludeTableList.contains(tableName.toLowerCase()) ||
					excludeTableList.contains(tableName.toUpperCase())) {
				continue;
			}
			List<TableFieldInfo> fieldList = tableInfo.getFieldList();
			for (TableFieldInfo fieldInfo : fieldList) {
				String column = fieldInfo.getColumn();
				if (column.equals(column)) {
					tenantTableList.add(tableName);
					continue tableFor;
				}
			}
		}
	}
}
