package com.tgb.util;

import hlc.base.db.DbAccess;

import java.util.List;
import java.util.Map;

public class CrtXmlMapperSql {
	public static void createInsertSql(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String colunms = "";
		String values = "";
		for (Map<String, String> mapColumn : columnList) {
			colunms += "," + mapColumn.get("COLUMN_NAME").toUpperCase();
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";
			}
			values += ",#{" + mapColumn.get("COLUMN_NAME").toUpperCase()
					+ ",jdbcType=" + jdbcType + "}";
		}
		colunms = colunms.substring(1, colunms.length());
		values = values.substring(1, values.length());
		System.out.println(tablename + "insert: = insert into " + tablename
				+ "(" + colunms + ") values (" + values + ")");
	}

	public static String createInsertSqlSel(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String colunms = "";
		String values = "";
		for (Map<String, String> mapColumn : columnList) {
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";
				colunms += " <if test=\""
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ " != null and "
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ "!='' \" >"
						+ mapColumn.get("COLUMN_NAME").toUpperCase() + ",</if>"; // mapColumn.get("COLUMN_NAME").toUpperCase();
				values += "<if test=\""
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ " != null and "
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ "!=''\" >#{"
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ ",jdbcType=" + jdbcType + "},</if>"; // mapColumn.get("COLUMN_NAME").toUpperCase()
																// + "}";

			} else if ("DECIMAL".equals(jdbcType)) {
				colunms += " <if test=\""
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ " != null and "
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ "!='' \" >"
						+ mapColumn.get("COLUMN_NAME").toUpperCase() + ",</if>"; // mapColumn.get("COLUMN_NAME").toUpperCase();
				values += "<if test=\""
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ " != null and "
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ "!=''\" >#{"
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ ",jdbcType=" + jdbcType + "},</if>"; // mapColumn.get("COLUMN_NAME").toUpperCase()
																// + "}";

			} else {
				colunms += " <if test=\""
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ " != null \" >"
						+ mapColumn.get("COLUMN_NAME").toUpperCase() + ",</if>"; // mapColumn.get("COLUMN_NAME").toUpperCase();
				values += "<if test=\""
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ " != null\" >#{"
						+ mapColumn.get("COLUMN_NAME").toUpperCase()
						+ ",jdbcType=" + jdbcType + "},</if>"; // mapColumn.get("COLUMN_NAME").toUpperCase()
																// + "}";

			}

		}
		// colunms = colunms.substring(1, colunms.length());
		// values = values.substring(1, values.length());
		/*
		 * System.out.println(tablename+" =insertSel: insert into " + tablename
		 * + " <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
		 * colunms +
		 * " </trim> <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" > "
		 * + values + "</trim>");
		 */
		String res = " insert into "
				+ tablename
				+ " <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >"
				+ colunms
				+ " </trim> <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" > "
				+ values + "</trim>";
		return res;
	}

	public static String createUpdateSqlSel(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String setColumns = "";
		String priKey = "";
		for (Map<String, String> mapColumn : columnList) {
			String key = mapColumn.get("COLUMN_KEY");
			String set = mapColumn.get("COLUMN_NAME").toUpperCase();
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";

				if (null != key && "PRI".equals(key)) {
					priKey += " and " + set + "=" + "#{" + set + ",jdbcType="
							+ jdbcType + "}";
				} else {
					setColumns += "<if test=\"" + set + " != null and " + set
							+ "!='' \">" + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "},</if>";
					// + set+"=#{"+set+"}";
				}
			} else if ("DECIMAL".equals(jdbcType)) {
				if (null != key && "PRI".equals(key)) {
					priKey += " and " + set + "=" + "#{" + set + ",jdbcType="
							+ jdbcType + "}";
				} else {
					setColumns += "<if test=\"" + set + " != null and " + set
							+ "!='' \">" + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "},</if>";
					// + set+"=#{"+set+"}";
				}
			} else {
				if (null != key && "PRI".equals(key)) {
					priKey += " and " + set + "=" + "#{" + set + ",jdbcType="
							+ jdbcType + "}";
				} else {
					setColumns += "<if test=\"" + set + " != null \">" + set
							+ "=#{" + set + ",jdbcType=" + jdbcType + "},</if>";
					// + set+"=#{"+set+"}";
				}
			}

		}
		// setColumns = setColumns.substring(1, setColumns.length());
		priKey = priKey.substring(4, priKey.length());
		/*
		 * System.out.println(tablename+" =updateSel: update " + tablename +
		 * " <set>" + setColumns + "</set> where " + priKey + " \r\n");
		 */
		String res = "update " + tablename + " <set>" + setColumns
				+ "</set> where " + priKey;
		return res;
	}

	public static String createSelectSqlSel(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String setColumns = "";
		String priKey = "";
		for (Map<String, String> mapColumn : columnList) {
			String key = mapColumn.get("COLUMN_KEY");
			String set = mapColumn.get("COLUMN_NAME").toUpperCase();
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";
				if (null != key && "PRI".equals(key)) {
					setColumns += "," + set;
					priKey += "<if test=\"" + set + " != null and " + set
							+ "!=''\"> and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else if ("DECIMAL".equals(jdbcType)) {
				if (null != key && "PRI".equals(key)) {
					setColumns += "," + set;
					priKey += "<if test=\"" + set + " != null and " + set
							+ "!=''\"> and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else {
				if (null != key && "PRI".equals(key)) {
					setColumns += "," + set;
					priKey += "<if test=\"" + set + " != null\"> and " + set
							+ "=#{" + set + ",jdbcType=" + jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			}

		}
		setColumns = setColumns.substring(1, setColumns.length());
		// priKey = priKey.substring(4, priKey.length());
		/*
		 * System.out.println(tablename+" =selectSel: select " + setColumns +
		 * " where " + priKey + "");
		 */
		String res = " select " + setColumns + " from " + tablename
				+ " <where> " + priKey + "</where>";
		return res;
	}

	public static String createSelectSqlSelByPk(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String setColumns = "";
		String priKey = "";
		for (Map<String, String> mapColumn : columnList) {
			String key = mapColumn.get("COLUMN_KEY");
			String set = mapColumn.get("COLUMN_NAME").toUpperCase();
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";
				if (null != key && "PRI".equals(key)) {
					priKey += "<if test=\"" + set + " != null and " + set
							+ "!=''\"> and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else if ("DECIMAL".equals(jdbcType)) {
				if (null != key && "PRI".equals(key)) {
					priKey += "<if test=\"" + set + " != null and " + set
							+ "!=''\"> and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else {
				if (null != key && "PRI".equals(key)) {
					priKey += "<if test=\"" + set + " != null\"> and " + set
							+ "=#{" + set + ",jdbcType=" + jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			}

		}
		setColumns = setColumns.substring(1, setColumns.length());
		// priKey = priKey.substring(4, priKey.length());
		/*
		 * System.out.println(tablename+" =selectSel: select " + setColumns +
		 * " where " + priKey + "");
		 */
		String res = " select " + setColumns + " from " + tablename
				+ " <where> " + priKey + "</where>";
		return res;
	}

	public static String createDeleteSqlSel(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String setColumns = "";
		String priKey = "";
		for (Map<String, String> mapColumn : columnList) {
			String key = mapColumn.get("COLUMN_KEY");
			String set = mapColumn.get("COLUMN_NAME").toUpperCase();
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";
				if (null != key && "PRI".equals(key)) {
					priKey += "<if test=\"" + set + " != null and " + set
							+ "!=''\"> and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else if ("DECIMAL".equals(jdbcType)) {
				if (null != key && "PRI".equals(key)) {
					priKey += "<if test=\"" + set + " != null and " + set
							+ "!=''\"> and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else {
				if (null != key && "PRI".equals(key)) {
					priKey += "<if test=\"" + set + " != null\"> and " + set
							+ "=#{" + set + ",jdbcType=" + jdbcType + "}</if>";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			}

		}
		setColumns = setColumns.substring(1, setColumns.length());
		// priKey = priKey.substring(4, priKey.length());
		/*
		 * System.out.println(tablename+" =deleteSel: delete from " +tablename +
		 * " where " + priKey + "");
		 */
		String res = "delete from " + tablename + " <where> " + priKey
				+ "</where>";
		return res;
	}

	public static String createDeleteSqlSelByPk(String sql, String tablename)
			throws Exception {

		DbAccess dbm = new DbAccess();
		List<Map<String, String>> columnList;
		columnList = dbm.queryForList(sql);
		String setColumns = "";
		String priKey = "";
		for (Map<String, String> mapColumn : columnList) {
			String key = mapColumn.get("COLUMN_KEY");
			String set = mapColumn.get("COLUMN_NAME").toUpperCase();
			String jdbcType = mapColumn.get("DATA_TYPE").toUpperCase();
			if ("INT".equals(jdbcType)) {
				jdbcType = "INTEGER";
				if (null != key && "PRI".equals(key)) {
					priKey += " and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else if ("DECIMAL".equals(jdbcType)) {
				if (null != key && "PRI".equals(key)) {
					priKey += " and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			} else {
				if (null != key && "PRI".equals(key)) {
					priKey += " and " + set + "=#{" + set + ",jdbcType="
							+ jdbcType + "}";
				} else {
					setColumns += "," + set;
					// + set+"=#{"+set+"}";
				}
			}
		}
		setColumns = setColumns.substring(1, setColumns.length());
		priKey = priKey.substring(4, priKey.length());
		/*
		 * System.out.println(tablename+" =deleteSel: delete from " +tablename +
		 * " where " + priKey + "");
		 */
		String res = "delete from " + tablename + " where " + priKey + "";
		return res;
	}

	public static void getXml(String tableName) throws Exception {
		String sql = "select DISTINCT COLUMN_KEY,COLUMN_NAME,DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA='lrd' and"
				+ " table_name='" + tableName + "'";
		String xmlhead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"><mapper namespace=\"com.tgb.mapper.cust."
				+ tableName + "Mapper\">";
		String select = "<select id=\"query\" parameterType=\"java.util.Map\" resultType=\"java.util.HashMap\">"
				+ CrtXmlMapperSql.createSelectSqlSel(sql, tableName)
				+ "</select>";
		/*String save = "<insert id=\"save\" parameterType=\"java.util.Map\">"
				+ CrtXmlMapperSql.createInsertSqlSel(sql, tableName)
				+ "</insert>";
		String update = "<update id=\"update\" parameterType=\"java.util.Map\">"
				+ CrtXmlMapperSql.createUpdateSqlSel(sql, tableName)
				+ "</update>";*/
		/*String deleteByPk = "<delete id=\"deleteByPk\">"
				+ CrtXmlMapperSql.createDeleteSqlSelByPk(sql, tableName)
				+ "</delete>";*/
		String delete = "<delete id=\"delete\">"
				+ CrtXmlMapperSql.createDeleteSqlSel(sql, tableName)
				+ "</delete>";
		String xmlEnd = "</mapper>";
		System.out.println(tableName + ":" + xmlhead + select /*+ save + update*/
				/*+ deleteByPk*/ + delete + xmlEnd);
	}

	public static void getViewXml(String tableName) throws Exception {
		String sql = "select DISTINCT COLUMN_KEY,COLUMN_NAME,DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA='lrd' and"
				+ " table_name='" + tableName + "'";
		String xmlhead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"><mapper namespace=\"com.tgb.mapper.cust."
				+ tableName + "Mapper\">";
		String select = "<select id=\"query\" parameterType=\"java.util.Map\" resultType=\"java.util.HashMap\">"
				+ CrtXmlMapperSql.createSelectSqlSel(sql, tableName)
				+ "</select>";

		String xmlEnd = "</mapper>";
		System.out.println(tableName + ":" + xmlhead + select + xmlEnd);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * CreateInsertSql.createSelectSqlSel("cust_base");
		 * CreateInsertSql.createDeleteSqlSel("cust_base");
		 * CreateInsertSql.createInsertSql("cust_base");
		 * CreateInsertSql.createUpdateSqlSel("cust_base");
		 * CreateInsertSql.createInsertSqlSel("cust_base");
		 */
		/*
		 * String s = String.format("%08d", 1); System.out.println(s);
		 * CrtXmlMapperSql.getXml("cust_dg"); CrtXmlMapperSql.getXml("cust_zz");
		 * CrtXmlMapperSql.getXml("cust_yz"); CrtXmlMapperSql.getXml("cust_js");
		 * CrtXmlMapperSql.getXml("cust_rela");
		 * CrtXmlMapperSql.getXml("cust_base");
		 * CrtXmlMapperSql.getXml("cust_zg"); CrtXmlMapperSql.getXml("fin_rpt");
		 * CrtXmlMapperSql.getXml("rpt_dtl");
		 * CrtXmlMapperSql.getXml("fin_trans");
		 * CrtXmlMapperSql.getXml("cust_feedback");
		 * CrtXmlMapperSql.getXml("cust_grade");
		 * CrtXmlMapperSql.getXml("cust_imp_date");
		 * CrtXmlMapperSql.getXml("cust_event_date");
		 * CrtXmlMapperSql.getXml("cust_doc");
		 * CrtXmlMapperSql.getXml("cust_rating");
		 * CrtXmlMapperSql.getXml("lrd_dic");
		 * CrtXmlMapperSql.getXml("CUST_APPLY");
		 * CrtXmlMapperSql.getXml("lrd_org");
		 * CrtXmlMapperSql.getXml("lrd_function");*/
		CrtXmlMapperSql.getXml("scsp");
		
	}

}
