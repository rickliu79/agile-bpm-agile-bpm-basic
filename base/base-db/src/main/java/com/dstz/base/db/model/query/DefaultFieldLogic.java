package com.dstz.base.db.model.query;

import java.util.ArrayList;
import java.util.List;

import com.dstz.base.api.query.FieldLogic;
import com.dstz.base.api.query.FieldRelation;
import com.dstz.base.api.query.WhereClause;

/**
 * @author csx
 */
public class DefaultFieldLogic implements FieldLogic {

	/**
	 * 查询字段组合列表
	 */
	private List<WhereClause> whereClauses = new ArrayList<WhereClause>();
	/**
	 * 字段关系
	 */
	private FieldRelation fieldRelation = FieldRelation.AND;

	public DefaultFieldLogic() {
	}

	public DefaultFieldLogic(FieldRelation fieldRelation) {
		this.fieldRelation = fieldRelation;
	}

	public List<WhereClause> getWhereClauses() {
		return whereClauses;
	}

	public void setWhereClauses(List<WhereClause> whereClauses) {
		this.whereClauses = whereClauses;
	}

	public FieldRelation getFieldRelation() {
		return fieldRelation;
	}

	public void setFieldRelation(FieldRelation fieldRelation) {
		this.fieldRelation = fieldRelation;
	}

	public String getSql() {
		if (whereClauses.isEmpty()) {
			return "";
		}

		StringBuilder sqlBuf = new StringBuilder("(");
		if (!whereClauses.isEmpty() && FieldRelation.NOT == fieldRelation) {
			sqlBuf.append(" NOT (");
			for (WhereClause clause : whereClauses) {
				if (!sqlBuf.toString().endsWith("NOT (")) {
					sqlBuf.append(" ").append(FieldRelation.AND).append(" ");
				}
				sqlBuf.append(clause.getSql());
			}
			sqlBuf.append(")");
			return sqlBuf.toString();
		}

		for (WhereClause clause : whereClauses) {
			if (!sqlBuf.toString().endsWith("(")) {
				sqlBuf.append(" ").append(fieldRelation).append(" ");
			}
			sqlBuf.append(clause.getSql());
		}
		sqlBuf.append(")");

		return sqlBuf.toString();
	}

}
