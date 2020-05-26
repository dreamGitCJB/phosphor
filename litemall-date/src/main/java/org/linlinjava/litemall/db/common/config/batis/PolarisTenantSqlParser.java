/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
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
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.linlinjava.litemall.db.common.config.batis;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.linlinjava.litemall.db.common.constans.StringPool;
import org.linlinjava.litemall.db.common.util.AuthUtil;
import org.linlinjava.litemall.db.common.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 多租户增强配置
 *
 * @author Chill
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PolarisTenantSqlParser extends TenantSqlParser {
	/**
	 * 租户处理器
	 */
	private TenantHandler tenantHandler;
	/**
	 * 超管需要启用租户过滤的表
	 */
	private List<String> adminTenantTables = Collections.singletonList("blade_dict_biz");

	@Override
	public TenantSqlParser setTenantHandler(TenantHandler tenantHandler) {
		super.setTenantHandler(tenantHandler);
		this.tenantHandler = tenantHandler;
		return this;
	}

	/**
	 * insert 语句处理
	 */
	@Override
	public void processInsert(Insert insert) {
		// 未启用租户增强，则使用原版逻辑
//		if (!properties.getEnhance()) {
//			super.processInsert(insert);
//			return;
//		}
		if (tenantHandler.doTableFilter(insert.getTable().getName())) {
			// 过滤退出执行
			return;
		}
		// 根据值自动配置租户字段
		boolean hasTenantColumn = hasTenantColumn(insert.getColumns());
		if (!hasTenantColumn) {
			insert.getColumns().add(new Column(tenantHandler.getTenantIdColumn()));
		}
		if (insert.getSelect() != null && !hasTenantColumn) {
			processPlainSelect((PlainSelect) insert.getSelect().getSelectBody(), true);
		} else if (insert.getItemsList() != null && !hasTenantColumn) {
			// fixed github pull/295
			ItemsList itemsList = insert.getItemsList();
			if (itemsList instanceof MultiExpressionList) {
				((MultiExpressionList) itemsList).getExprList().forEach(el -> el.getExpressions().add(tenantHandler.getTenantId(false)));
			} else {
				((ExpressionList) insert.getItemsList()).getExpressions().add(tenantHandler.getTenantId(false));
			}
		} else if (!hasTenantColumn) {
			throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId");
		}
	}

	/**
	 * 处理 PlainSelect
	 *
	 * @param plainSelect ignore
	 * @param addColumn   是否添加租户列,insert into select语句中需要
	 */
	@Override
	protected void processPlainSelect(PlainSelect plainSelect, boolean addColumn) {
		FromItem fromItem = plainSelect.getFromItem();
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			if (!tenantHandler.doTableFilter(fromTable.getName())) {
				// 若是超管则不进行过滤
				if (!doTenantFilter(fromTable.getName())) {
					//#1186 github
					plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
					if (addColumn) {
						plainSelect.getSelectItems().add(new SelectExpressionItem(new Column(tenantHandler.getTenantIdColumn())));
					}
				}
			}
		} else {
			processFromItem(fromItem);
		}
		List<Join> joins = plainSelect.getJoins();
		if (joins != null && joins.size() > 0) {
			joins.forEach(j -> {
				processJoin(j);
				processFromItem(j.getRightItem());
			});
		}
	}

	/**
	 * update 语句处理
	 */
	@Override
	public void processUpdate(Update update) {
		final Table table = update.getTable();
		if (tenantHandler.doTableFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		if (doTenantFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		update.setWhere(this.andExpression(table, update.getWhere()));
	}

	/**
	 * delete 语句处理
	 */
	@Override
	public void processDelete(Delete delete) {
		final Table table = delete.getTable();
		if (tenantHandler.doTableFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		if (doTenantFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		delete.setWhere(this.andExpression(table, delete.getWhere()));
	}

	/**
	 * delete update 语句 where 处理
	 */
	@Override
	protected BinaryExpression andExpression(Table table, Expression where) {
		//获得where条件表达式
		EqualsTo equalsTo = new EqualsTo();
		Expression leftExpression = this.getAliasColumn(table);
		Expression rightExpression = tenantHandler.getTenantId(true);
		// 若是超管则不进行过滤
		if (doTenantFilter(table.getName())) {
			leftExpression = rightExpression = new StringValue(StringPool.ONE);
		}
		equalsTo.setLeftExpression(leftExpression);
		equalsTo.setRightExpression(rightExpression);
		if (null != where) {
			if (where instanceof OrExpression) {
				return new AndExpression(equalsTo, new Parenthesis(where));
			} else {
				return new AndExpression(equalsTo, where);
			}
		}
		return equalsTo;
	}

	/**
	 * 增强插件使超级管理员可以看到所有租户数据
	 */
	@Override
	protected Expression builderExpression(Expression currentExpression, Table table) {
		final Expression tenantExpression = this.getTenantHandler().getTenantId(false);
		Expression appendExpression;
		if (!(tenantExpression instanceof SupportsOldOracleJoinSyntax)) {
			appendExpression = new EqualsTo();
			Expression leftExpression = this.getAliasColumn(table);
			Expression rightExpression = tenantExpression;
			// 若是超管则不进行过滤
			if (doTenantFilter(table.getName())) {
				leftExpression = rightExpression = new StringValue(StringPool.ONE);
			}
			((EqualsTo) appendExpression).setLeftExpression(leftExpression);
			((EqualsTo) appendExpression).setRightExpression(rightExpression);
		} else {
			appendExpression = processTableAlias4CustomizedTenantIdExpression(tenantExpression, table);
		}
		if (currentExpression == null) {
			return appendExpression;
		}
		if (currentExpression instanceof BinaryExpression) {
			BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
			if (binaryExpression.getLeftExpression() instanceof FromItem) {
				processFromItem((FromItem) binaryExpression.getLeftExpression());
			}
			if (binaryExpression.getRightExpression() instanceof FromItem) {
				processFromItem((FromItem) binaryExpression.getRightExpression());
			}
		} else if (currentExpression instanceof InExpression) {
			InExpression inExp = (InExpression) currentExpression;
			ItemsList rightItems = inExp.getRightItemsList();
			if (rightItems instanceof SubSelect) {
				processSelectBody(((SubSelect) rightItems).getSelectBody());
			}
		}
		if (currentExpression instanceof OrExpression) {
			return new AndExpression(new Parenthesis(currentExpression), appendExpression);
		} else {
			return new AndExpression(currentExpression, appendExpression);
		}
	}

	/**
	 * 是否具有租户字段
	 *
	 * @param columns 字段集合
	 */
	public boolean hasTenantColumn(List<Column> columns) {
		Optional<Column> result = columns.stream().filter(column ->
			StringUtil.equalsIgnoreCase(column.getColumnName(), tenantHandler.getTenantIdColumn())
		).findFirst();
		Column column = result.orElse(null);
		return column != null;
	}

	/**
	 * 判断当前操作是否需要进行过滤
	 *
	 * @param tableName 表名
	 */
	public boolean doTenantFilter(String tableName) {
		return AuthUtil.isAdministrator() && !adminTenantTables.contains(tableName);
	}


}
