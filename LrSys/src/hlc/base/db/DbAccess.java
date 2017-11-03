package hlc.base.db;

import hlc.base.DbAccessHelper;
import hlc.base.EnBase;
import hlc.base.EnField;
import hlc.util.HlcException;
import hlc.util.MultiPage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class DbAccess {

	private static boolean Instantiation = false;// 是否已经实例化
	private boolean autoCommit;
	private ConnSet cs;// 数据库连接
	private Connection currentConnection;
	private String dbName;
	private static String logPath = "c:";
	private static Hashtable mapNameConnSet = new Hashtable();// 数据库连接池
	private HashMap mapPreparePara;
	private static int maxConnCount = 20;
	private String prepareSQL;
	private PreparedStatement ps;
	private Statement stmt;
	private static int timeout = 0;

	public DbAccess(String dbName) throws Exception {
		cs = null;
		currentConnection = null;
		stmt = null;
		ps = null;
		prepareSQL = "";
		mapPreparePara = null;
		this.dbName = "";
		autoCommit = true;
		this.dbName = dbName.toLowerCase();
		autoCommit = true;
		init();
	}

	public DbAccess() throws Exception {
		cs = null;
		currentConnection = null;
		stmt = null;
		ps = null;
		prepareSQL = "";
		mapPreparePara = null;
		dbName = "";
		autoCommit = true;
		autoCommit = true;
		init();
	}

	public DbAccess(boolean b) throws Exception {
		cs = null;
		currentConnection = null;
		stmt = null;
		ps = null;
		prepareSQL = "";
		mapPreparePara = null;
		dbName = "";
		autoCommit = b;
		init();
	}

	private void init() throws Exception {
		if (cs == null) {
			if (!Instantiation) {
				InputStream is = getClass()
						.getResourceAsStream("/CrtDBConfig.xml");
				DOMParser dp = new DOMParser();
				if (is == null)
					throw new HlcException("01");
				dp.parse(new InputSource(is));
				Document doc = dp.getDocument();
				NodeList globeList = doc.getElementsByTagName("globe");
				Element globe = (Element) globeList.item(0);
				maxConnCount = Integer
						.parseInt(globe.getAttribute("connCount"));
				timeout = Integer.parseInt(globe.getAttribute("timeOut"));
				logPath = globe.getAttribute("logPath");
				NodeList dbList = doc.getElementsByTagName("db");
				int dbcount = dbList.getLength();
				for (int i = 0; i < dbcount; i++) {
					Element db = (Element) dbList.item(i);
					ConnSet csTmp = new ConnSet();
					if (db.getAttribute("isDefault").equals("true"))
						csTmp.isDefault = true;
					if (db.getAttribute("isOtherDataSource").equals("true"))
						csTmp.isOtherDataSource = true;
					csTmp.dbName = db.getElementsByTagName("dbName").item(0)
							.getFirstChild().getNodeValue().toLowerCase();

					if (mapNameConnSet.containsKey(csTmp.dbName))
						throw new HlcException("11", csTmp.dbName);
					if (db.getElementsByTagName("transactionIsolation").item(0)
							.getFirstChild() != null)
						csTmp.transactionIsolation = Integer.parseInt(db
								.getElementsByTagName("transactionIsolation")
								.item(0).getFirstChild().getNodeValue());
					if (db.getElementsByTagName("dbCharset").item(0)
							.getFirstChild() != null)
						csTmp.dbCharset = db.getElementsByTagName("dbCharset")
								.item(0).getFirstChild().getNodeValue();
					DataSet.dbCharset = csTmp.dbCharset;
					if (db.getElementsByTagName("fileEncoding").item(0)
							.getFirstChild() != null)
						csTmp.fileEncoding = db
								.getElementsByTagName("fileEncoding").item(0)
								.getFirstChild().getNodeValue();
					if (csTmp.isOtherDataSource) {
						csTmp.jndiName = db.getElementsByTagName("jndiName")
								.item(0).getFirstChild().getNodeValue();
					} else {
						csTmp.dbDriver = db.getElementsByTagName("dbDriver")
								.item(0).getFirstChild().getNodeValue();
						csTmp.connStr = db.getElementsByTagName("connStr")
								.item(0).getFirstChild().getNodeValue();
						csTmp.checkCode = 0;
						csTmp.user = db.getElementsByTagName("user").item(0)
								.getFirstChild().getNodeValue();
						if (db.getElementsByTagName("password").item(0)
								.getFirstChild() != null)
							csTmp.password = db
									.getElementsByTagName("password").item(0)
									.getFirstChild().getNodeValue();
					}
					mapNameConnSet.put(csTmp.dbName, csTmp);
					csTmp.isSQLServer = csTmp.dbDriver.toLowerCase().indexOf(
							"sqlserver.") > -1;
					csTmp.isInformix = csTmp.dbDriver.toLowerCase().indexOf(
							"informix.") > -1;
					csTmp.isDB2 = csTmp.dbDriver.toLowerCase().indexOf("db2.") > -1;
					csTmp.isOracle = csTmp.dbDriver.toLowerCase().indexOf(
							"oracle.") > -1;
					csTmp.isMySQL = csTmp.dbDriver.toLowerCase().indexOf(
							"mysql.") > -1;
					csTmp.isSybase = csTmp.dbDriver.toLowerCase().indexOf(
							"sybase.") > -1;
					csTmp.isODBC = csTmp.dbDriver.toLowerCase()
							.indexOf("odbc.") > -1;
				}
				is.close();
				Instantiation = true;
			}
			cs = findConnSetBydbName();// 获得当数据库连接信息
			cs.isSameCharset = cs.dbCharset.equalsIgnoreCase(cs.fileEncoding);
			dbName = cs.dbName;
			if (!cs.isOtherDataSource)
				Class.forName(cs.dbDriver);
		}
	}

	private DataSet RStoDS(ResultSet rs, int startRow, int rowCount)
			throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		DataField dataField[] = new DataField[numCols];
		int dataType[] = new int[numCols];
		for (int i = 0; i < numCols; i++) {
			dataField[i] = new DataField();
			dataField[i].setName(rsmd.getColumnName(i + 1));
			dataField[i].setAlias(rsmd.getColumnName(i + 1));
			dataField[i].setSqlType(rsmd.getColumnTypeName(i + 1));// 数据库中对应的数据类型
			dataField[i].setClassName(rsmd.getColumnClassName(i + 1));// 对应的java类型
			dataField[i].setDisplaySize(rsmd.getColumnDisplaySize(i + 1));// 字段长度
			dataField[i].setScale(rsmd.getScale(i + 1));
			dataType[i] = DataField.getColumnTypeByClassName(dataField[i]
					.getClassName());
			dataField[i].setType(dataType[i]);
		}

		int cnt = -1;
		DataSet dsData = new DataSet(dataField);
		int i = 0;
		while (rs.next()) {
			cnt++;
			if (rowCount > 0) {
				if (cnt < startRow)
					continue;
				if (cnt >= startRow + rowCount)
					break;
			}
			ArrayList columns = createDataRow(rs, dataType, numCols);
			dsData.addRow(columns);
		}
		return dsData;
	}

	private EnBase[] RStoEntityArray(ResultSet rs, int startRow, int rowCount,
			String className) throws Exception {
		ArrayList alColumns = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount(); i++)
			alColumns.add(rsmd.getColumnName(i + 1).toUpperCase());

		int cnt = 0;
		ArrayList al = new ArrayList();
		while (rs.next()) {
			cnt++;
			if (rowCount > 0) {
				if (cnt < startRow)
					continue;
				if (cnt >= startRow + rowCount)
					break;
			}
			EnBase en = (EnBase) Class.forName(className).newInstance();
			EnField aEntityField[] = en.getFieldArray();
			for (int i = 0; i < aEntityField.length; i++)
				if (!aEntityField[i].getDataType().equals("AbstractEntity")) {
					String fieldName = aEntityField[i].getFieldName();
					if (alColumns.contains(fieldName.toUpperCase())) {
						Object objValue = rs.getObject(fieldName);
						if (objValue != null) {
							if (objValue instanceof String)
								objValue = rightTrim(objValue.toString());
							en.setValue(fieldName, objValue);
						}
					}
				}

			al.add(en);
		}
		Object a = Array.newInstance(Class.forName(className), al.size());
		for (int i = 0; i < Array.getLength(a); i++)
			Array.set(a, i, al.get(i));

		return (EnBase[]) a;
	}

	public static void addDBConfig(String dbName, String dbDriver,
			String connStr, int transactionIsolation, String user,
			String password, String dbCharset, String fileEncoding) {
		removeDBConfig(dbName);
		ConnSet csTmp = new ConnSet();
		csTmp.dbName = dbName;
		csTmp.dbDriver = dbDriver;
		csTmp.connStr = connStr;
		csTmp.transactionIsolation = transactionIsolation;
		csTmp.dbCharset = dbCharset;
		csTmp.fileEncoding = fileEncoding;
		csTmp.user = user;
		csTmp.password = password;
		csTmp.isSameCharset = csTmp.dbCharset
				.equalsIgnoreCase(csTmp.fileEncoding);
		mapNameConnSet.put(dbName.toLowerCase(), csTmp);
	}

	public void beginTransaction() throws SQLException {
		autoCommit = false;
		if (currentConnection != null)
			currentConnection.setAutoCommit(false);
	}

	public void cancelQuery() throws Exception {
		if (stmt != null)
			stmt.cancel();
		if (ps != null)
			ps.cancel();
	}

	private boolean checkConnection(Connection cc) throws Exception {
		if (cc == null)
			return false;
		if (!autoCommit)
			return true;
		try {
			Statement smtest = cc.createStatement();
			smtest.executeQuery("select user()");
			smtest.close();
			return true;
		} catch (SQLException e) {
			// if(e.getErrorCode() == cs.checkCode)
			// {
			// return true;
			// } else
			// {
			cc = null;
			return false;
			// }
		}
	}

	public static void clearPool() throws Exception {

		Object aObjSet[] = mapNameConnSet.values().toArray();
		ConnSet aConnSet[] = new ConnSet[aObjSet.length];
		for (int i = 0; i < aObjSet.length; i++)
			aConnSet[i] = (ConnSet) aObjSet[i];
		try {
			for (int i = 0; i < aConnSet.length; i++) {
				ConnSet csTmp = aConnSet[i];
				Object aObjConn[] = csTmp.mapConn.keySet().toArray();
				Connection aConn[] = new Connection[aObjConn.length];
				for (int j = 0; j < aObjConn.length; j++)
					aConn[j] = (Connection) aObjConn[j];

				for (int j = 0; j < aConn.length; j++) {
					if (!aConn[j].getAutoCommit())
						aConn[j].rollback();
					aConn[j].close();
					csTmp.mapConn.remove(aConn[j]);
					aConn[j] = null;
				}

			}

			mapNameConnSet.clear();
		} catch (SQLException e) {
			String msg = "\r\n  Operation:  clear pool\r\n  Exception:   "
					+ e.toString();
			writeLog("error", msg);
			throw new HlcException("06", e.toString());
		}
		return;
	}

	private void closeStatement() throws SQLException {
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (isPrepared()) {
			prepareSQL = "";
			mapPreparePara = null;
			ps.close();
			ps = null;
		}
	}

	public void commit() throws Exception {
		currentConnection.commit();
		freeConnection();
	}

	private ArrayList createDataRow(ResultSet rs, int dataType[], int numCols)
			throws Exception {
		ArrayList row = new ArrayList();
		for (int j = 1; j <= numCols; j++) {
			switch (dataType[j - 1]) {
			case 1: // '\001'
				if (isODBC()) {
					row.add(rs.getBytes(j));
					break;
				}
				String tmp = rs.getString(j);
				if (tmp == null)
					tmp = "";
				row.add(tmp.getBytes(cs.dbCharset));
				break;

			case 4: // '\004'
				row.add(new Integer(rs.getInt(j)));
				break;

			case 8: // '\b'
				row.add(new Double(rs.getDouble(j)));
				break;

			case -5:
				row.add(new Long(rs.getLong(j)));
				break;

			case 3: // '\003'
				BigDecimal bd = rs.getBigDecimal(j);
				if (bd == null)
					row.add(null);
				else
					row.add(String.valueOf(bd).getBytes());
				break;

			case 91: // '['
				Date date = rs.getDate(j);
				if (date == null)
					row.add(date);
				else
					row.add(date.toString());
				break;

			case 92: // '\\'
				row.add(rs.getTime(j));
				break;

			case 93: // ']'
				row.add(rs.getTimestamp(j));
				break;

			case -2:
				row.add(rs.getBytes(j));
				break;

			default:
				row.add(rs.getObject(j));
				break;
			}
		}

		return row;
	}

	/*
	 * public EnBase[] executePreparedQuery(String className) throws Exception {
	 * if(className == null || className.equals("")) throw new
	 * HlcException("10"); else return executeQuery(null, className); }
	 */

	/*
	 * public DataSet executePreparedQuery() throws Exception { return
	 * executeQuery(null); }
	 * 
	 * public int executePreparedUpdate() throws Exception { return
	 * executeUpdate(null); }
	 */

	private Object executeQuery(String sql, int startRow, int rowCount,
			String className) throws Exception {
		Exception exception;
		Object obj;
		try {
			boolean isFirstTime = currentConnection == null;
			if (isFirstTime)
				currentConnection = getConnection();
			if (sql == null) {
				if (ps == null)
					throw new HlcException("03");
				sql = prepareSQL;
			}
			if (!cs.isSameCharset)
				sql = new String(sql.getBytes(cs.fileEncoding), cs.dbCharset);
			ResultSet rs;
			// if(currentConnection.isClosed())
			// currentConnection = makeConnection();
			try {
				if (isPrepared()) {
					rs = ps.executeQuery();
				} else {
					stmt = currentConnection.createStatement();
					rs = stmt.executeQuery(sql);
				}
			} catch (SQLException se) {
				if (!isFirstTime || isPrepared()
						|| checkConnection(currentConnection))
					throw getBusinessException(se);
				if (cs.isOtherDataSource) {
					currentConnection = getConnection();
				} else {
					cs.mapConn.remove(currentConnection);
					currentConnection = makeConnection();
					cs.mapConn.put(currentConnection, "1");
					currentConnection.setAutoCommit(autoCommit);
				}
				stmt = currentConnection.createStatement();
				rs = stmt.executeQuery(sql);
			}
			Object rObj = null;
			if (className == null)
				rObj = RStoDS(rs, startRow, rowCount);
			else
				rObj = RStoEntityArray(rs, startRow, rowCount, className);
			rs.close();
			closeStatement();
			obj = rObj;
		} catch (Exception ex) {
			String msg = "\r\n  Operation: Query\r\n        SQL: " + sql
					+ "\r\n";
			if (isPrepared()) {
				msg = msg + "       Data: " + mapPreparePara.toString()
						+ "\r\n";
				String a[] = sql.replace('?', '@').split("@");
				String sqlNew = "";
				for (int i = 0; i < a.length - 1; i++)
					sqlNew = sqlNew + a[i] + "'"
							+ mapPreparePara.get(String.valueOf(i + 1)) + "'";

				sqlNew = sqlNew + a[a.length - 1];
				msg = msg + "        SQL: " + sqlNew + "\r\n";
			}
			msg = msg + "  Exception: " + ex.toString();
			if (!cs.isSameCharset)
				msg = new String(msg.getBytes(cs.dbCharset), cs.fileEncoding);
			writeLog("error", msg);
			throw ex;
		} finally {
			if (autoCommit)
				freeConnection();
		}
		return obj;
	}

	public void executeTxt(String sql) throws Exception {
		try {
			boolean isFirstTime = currentConnection == null;
			if (isFirstTime)
				currentConnection = getConnection();
			if (sql == null) {
				if (ps == null)
					throw new HlcException("03");
				sql = prepareSQL;
			}
			if (!cs.isSameCharset)
				sql = new String(sql.getBytes(cs.fileEncoding), cs.dbCharset);
			try {
				
					stmt = currentConnection.createStatement();
					 stmt.execute(sql);//.executeQuery(sql);
				
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se);
				//return false;
			}

		} catch (Exception ex) {
			//return false;
			throw new Exception(ex);
		} finally {
			if (autoCommit)
				freeConnection();
		}
	}

	public EnBase[] executeQuery(String sql, String className) throws Exception {
		if (className == null || className.equals(""))
			throw new HlcException("10");
		else
			return executeQuery(sql, className, 0, 0);
	}

	public void executePageQuery(String sql, String className,
			MultiPage multiPage) throws Exception {
		if (className == null || className.equals(""))
			throw new HlcException("10");
		else {

			if (multiPage == null)
				throw new HlcException("10");

			// int totalCount = multiPage.getTotalCount();
			// if(totalCount==0){
			// 20140701 mc 添加MySQL数据库的验证
			if (this.isMySQL()) {
				DataSet ds = executeQuery("select count(*)+'' num from ( "
						+ sql + ") tab");
				multiPage
						.setTotalCount(Integer.parseInt(ds.getString(0, "num")));
			} else if (this.isDB2()) {
				DataSet ds = executeQuery("select count(*) num from ( " + sql
						+ ") tab");
				multiPage.setTotalCount(ds.getInt(0, "num"));
			} else {
				throw new Exception("本系统不支持该数据库");
			}
			// }

			int pageNum = multiPage.getPageNum();
			int startRow = (pageNum - 1) * multiPage.getNumPerPage() + 1;
			int rownum = multiPage.getNumPerPage();
			EnBase[] aEnBase = (EnBase[]) executeQuery(sql, startRow, rownum,
					className);
			multiPage.setEnArray(aEnBase);

		}
	}

	public DataSet executePageQuery(String sql, MultiPage cMultiPage)
			throws Exception {

		if (cMultiPage == null)
			throw new HlcException("10");
		// int totalCount = cMultiPage.getTotalCount();
		// if(totalCount==0){
		// 20140701 sbj 添加MySQL数据库的验证
		if (this.isMySQL()) {
			DataSet ds = executeQuery("select count(*)+'' num from ( " + sql
					+ ") tab");
			cMultiPage.setTotalCount(Integer.parseInt(ds.getString(0, "num")));
		} else if (this.isDB2()) {
			DataSet ds = executeQuery("select count(*) num from ( " + sql
					+ ") tab");
			cMultiPage.setTotalCount(ds.getInt(0, "num"));
		} else {
			throw new Exception("本系统不支持该数据库");
		}

		// }
		int pageNum = cMultiPage.getPageNum();
		int startRow = (pageNum - 1) * cMultiPage.getNumPerPage();
		int rownum = cMultiPage.getNumPerPage();
		return (DataSet) executeQuery(sql, startRow, rownum, null);
	}

	public DataSet executeQuery(String sql) throws Exception {
		return executeQuery(sql, 0, 0);
	}

	public DataSet executeQuery(String sql, int startRow, int rowCount)
			throws Exception {
		return (DataSet) executeQuery(sql, startRow, rowCount,
				((String) (null)));
	}

	public EnBase[] executeQuery(String sql, String className, int startRow,
			int rowCount) throws Exception {
		if (className == null || className.equals(""))
			throw new HlcException("10");
		else
			return (EnBase[]) executeQuery(sql, startRow, rowCount, className);
	}

	public int executeUpdate(String sql) throws Exception {
		boolean isFirstTime;
		int r;
		isFirstTime = currentConnection == null;
		String msg = null;
		if (isFirstTime)
			currentConnection = getConnection();
		if (sql == null) {
			sql = prepareSQL;
			if (ps == null)
				throw new HlcException("04");
		}
		r = 0;
		if (!cs.isSameCharset)
			sql = new String(sql.getBytes(cs.fileEncoding), cs.dbCharset);
		try {
			if (isPrepared()) {
				r = ps.executeUpdate();
			} else {
				stmt = currentConnection.createStatement();
				r = stmt.executeUpdate(sql);
			}
		} catch (SQLException ex) {
			msg = "\r\n  Operation: Update\r\n        SQL: " + sql + "\r\n";
			if (isPrepared()) {
				msg = msg + "       Data: " + mapPreparePara.toString()
						+ "\r\n";
				String a[] = sql.replace('?', '@').split("@");
				String sqlNew = "";
				for (int i = 0; i < a.length - 1; i++)
					sqlNew = sqlNew + a[i] + "'"
							+ mapPreparePara.get(String.valueOf(i + 1)) + "'";

				sqlNew = sqlNew + a[a.length - 1];
				msg = msg + "        SQL: " + sqlNew + "\r\n";
			}
			if (!cs.isSameCharset)
				msg = new String(msg.getBytes(cs.dbCharset), cs.fileEncoding);
			writeLog("error", msg);
			// if(!isFirstTime || isPrepared() ||
			// checkConnection(currentConnection))
			throw getBusinessException(ex);
		} catch (Exception ex) {
			msg = msg + "  Exception: " + ex.toString();
			if (!cs.isSameCharset)
				msg = new String(msg.getBytes(cs.dbCharset), cs.fileEncoding);
			writeLog("error", msg);
			throw ex;

		} finally {
			closeStatement();
			if (autoCommit)
				freeConnection();
		}
		return r;
	}

	public static boolean existDatabase(String dbName) {
		return mapNameConnSet.containsKey(dbName.toLowerCase());
	}

	private ConnSet findConnSetBydbName() throws Exception {
		Object obj = mapNameConnSet.get(dbName);
		if (obj == null) {
			Object aObjSet[] = mapNameConnSet.values().toArray();
			ConnSet aConnSet[] = new ConnSet[aObjSet.length];
			for (int i = 0; i < aObjSet.length; i++)
				aConnSet[i] = (ConnSet) aObjSet[i];

			for (int i = 0; i < aConnSet.length; i++) {
				if (aConnSet[i].isDefault) {
					dbName = aConnSet[i].dbName;
					return aConnSet[i];
				}
			}

			writeLog("error",
					"\r\n  Operation:  init\r\n  Exception: init, datasource name ["
							+ dbName + "] not found.");
			throw new HlcException("02", dbName);
		} else {
			return (ConnSet) obj;
		}
	}

	public synchronized void freeConnection() throws SQLException {
		closeStatement();
		if (cs.isOtherDataSource) {
			currentConnection.close();
			return;
		}
		if (currentConnection == null)
			return;
		try {
			if (!autoCommit)
				currentConnection.rollback();
		} catch (Exception ex) {
			autoCommit = true;
			if (!currentConnection.isClosed())
				currentConnection.setAutoCommit(true);
			cs.mapConn.put(currentConnection, "0");
			currentConnection = null;
			return;
		} finally {
			autoCommit = true;
			if (!currentConnection.isClosed())
				currentConnection.setAutoCommit(true);
			cs.mapConn.put(currentConnection, "0");
			currentConnection = null;
		}

	}

	public String getActiveConns() {
		Object aObjSet[] = mapNameConnSet.values().toArray();
		ConnSet aConnSet[] = new ConnSet[aObjSet.length];
		for (int i = 0; i < aObjSet.length; i++)
			aConnSet[i] = (ConnSet) aObjSet[i];

		String tmpConn = "";
		if (aConnSet.length == 0)
			tmpConn = "no connection now.";
		for (int i = 0; i < aConnSet.length; i++)
			tmpConn = tmpConn + aConnSet[i].dbName + "=----"
					+ aConnSet[i].mapConn + "----<br>\r\n";

		return tmpConn;
	}

	private SQLException getBusinessException(SQLException ex) {
		if (cs.isInformix
				&& (ex.getErrorCode() == -244 || ex.getErrorCode() == -245))
			return new SQLException(
					"System is busy now,Please try again later.");
		else
			return ex;
	}

	public synchronized Connection getConnection() throws Exception {
		if (currentConnection != null)
			return currentConnection;
		if (cs.isOtherDataSource) {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup(cs.jndiName);
			currentConnection = ds.getConnection();
			if (cs.transactionIsolation != currentConnection
					.getTransactionIsolation())
				currentConnection
						.setTransactionIsolation(cs.transactionIsolation);
		} else {
			long starttime = System.currentTimeMillis();
			synchronized (getClass()) {
				Object aConn[] = cs.mapConn.keySet().toArray();
				while (currentConnection == null) {
					int i;
					for (i = 0; i < aConn.length; i++) {
						if (cs.mapConn.get(aConn[i]).toString().equals("0")) {
							cs.mapConn.put(aConn[i], "1");
							currentConnection = (Connection) aConn[i];
							//System.out.println("******************************"+cs.mapConn.size());
							if (!checkConnection(currentConnection)) {
								currentConnection = makeConnection();
							}
							break;
						}
					}

					if (i < aConn.length)
						break;
					if (aConn.length < maxConnCount) {
						currentConnection = makeConnection();
						cs.mapConn.put(currentConnection, "1");
						break;
					}
					if (timeout > 0) {
						long overtime = System.currentTimeMillis() - starttime;
						if (overtime >= (long) timeout) {
							writeLog(
									"error",
									"\r\n  Operation:  connect to databse\r\n  Exception:  timeout:  value="
											+ timeout + "ms,wait "
											+ String.valueOf(overtime)
											+ "ms.\r\n");
							String msg[] = { String.valueOf(timeout),
									String.valueOf(overtime) };
							throw new HlcException("05", msg);
						}
					}
					Thread.sleep(10L);
				}
			}
		}
		if (!autoCommit)
			currentConnection.setAutoCommit(false);
		return currentConnection;
	}

	public synchronized Connection getCurrentConnection() throws Exception {
		if (currentConnection != null)
			currentConnection = getConnection();
		return currentConnection;
	}

	public static String getDBAttributeByName(String dbName, String attribute)
			throws Exception {
		Object aObjSet[] = mapNameConnSet.values().toArray();
		ConnSet aConnSet[] = new ConnSet[aObjSet.length];
		for (int i = 0; i < aObjSet.length; i++)
			aConnSet[i] = (ConnSet) aObjSet[i];

		for (int i = 0; i < aConnSet.length; i++) {
			ConnSet connSet = aConnSet[i];
			if (connSet.dbName.equalsIgnoreCase(dbName)) {
				if (attribute.equalsIgnoreCase("dbDriver"))
					return connSet.dbDriver;
				if (attribute.equalsIgnoreCase("connStr"))
					return connSet.connStr;
				if (attribute.equalsIgnoreCase("transactionIsolation"))
					return String.valueOf(connSet.transactionIsolation);
				if (attribute.equalsIgnoreCase("dbCharset"))
					return connSet.dbCharset;
				if (attribute.equalsIgnoreCase("fileEncoding"))
					return connSet.fileEncoding;
				if (attribute.equalsIgnoreCase("checkCode"))
					return String.valueOf(connSet.checkCode);
				if (attribute.equalsIgnoreCase("user"))
					return connSet.user;
				if (attribute.equalsIgnoreCase("password"))
					return connSet.password;
				if (attribute.equalsIgnoreCase("jndiName"))
					return connSet.jndiName;
				else
					throw new HlcException("08", attribute);
			}
		}

		throw new HlcException("09", dbName);
	}

	public boolean isDB2() {
		return cs.isDB2;
	}

	public boolean isInformix() {
		return cs.isInformix;
	}

	public boolean isMySQL() {
		return cs.isMySQL;
	}

	public boolean isODBC() {
		return cs.isODBC;
	}

	public boolean isOracle() {
		return cs.isOracle;
	}

	private boolean isPrepared() {
		return ps != null;
	}

	public boolean isSQLServer() {
		return cs.isSQLServer;
	}

	public boolean isSameCharset() {
		return cs.isSameCharset;
	}

	public boolean isSybase() {
		return cs.isSybase;
	}

	private Connection makeConnection() throws Exception {
		Connection c;
		Statement smtest = null;
		c = null;
		String tmpstr = cs.connStr;
		try {
			c = DriverManager.getConnection(tmpstr, cs.user, cs.password);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("获取数据库连接出错");
		}
		if (cs.isInformix) {
			Statement sm = c.createStatement();
			sm.execute("set lock mode to wait 10");
		}

		// cs.checkCode = e.getErrorCode();
		if (smtest != null)
			smtest.close();
		if (cs.transactionIsolation != c.getTransactionIsolation())
			c.setTransactionIsolation(cs.transactionIsolation);
		// LevinDBConnection lc = new LevinDBConnection(this, c);
		return c;
	}

	public void prepareSQL(String sql) throws Exception {
		/*
		 * boolean isFirstTime = currentConnection == null; if(currentConnection
		 * == null) currentConnection = getConnection(); ps =
		 * currentConnection.prepareStatement(sql); prepareSQL = sql;
		 * mapPreparePara = new HashMap(); break MISSING_BLOCK_LABEL_164;
		 * SQLException se; se; if(!isFirstTime ||
		 * checkConnection(currentConnection)) throw getBusinessException(se);
		 * if(cs.isOtherDataSource) { currentConnection = getConnection(); }
		 * else { cs.mapConn.remove(currentConnection); currentConnection =
		 * makeConnection(); cs.mapConn.put(currentConnection, "1");
		 * currentConnection.setAutoCommit(autoCommit); } return;
		 */
	}

	public static void removeDBConfig(String dbName) {
		Object aObjName[] = mapNameConnSet.keySet().toArray();
		for (int i = 0; i < aObjName.length; i++) {
			if (aObjName[i].toString().equalsIgnoreCase(dbName)) {
				mapNameConnSet.remove(aObjName[i]);
				break;
			}
		}

	}

	private static String rightTrim(String data) {
		int i;
		for (i = data.length() - 1; i >= 0; i--)
			if (data.charAt(i) != ' ')
				break;

		return data.substring(0, i + 1);
	}

	public void rollback() throws Exception {
		if (currentConnection != null)
			currentConnection.rollback();
		freeConnection();
	}

	public void setPrepareBigDecimal(int i, BigDecimal value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setBigDecimal(i, value);
			mapPreparePara.put(String.valueOf(i), value);
			return;
		}
	}

	public void setPrepareBinaryStream(int i, InputStream value, int len)
			throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setBinaryStream(i, value, len);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareBlob(int i, Blob value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setBlob(i, value);
			mapPreparePara.put(String.valueOf(i), value);
			return;
		}
	}

	public void setPrepareBoolean(int i, boolean value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setBoolean(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareByte(int i, byte value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setByte(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareBytes(int i, byte value[]) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setBytes(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareDouble(int i, double value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setDouble(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareFloat(int i, float value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setFloat(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareInt(int i, int value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setInt(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareLong(int i, long value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setLong(i, value);
			mapPreparePara.put(String.valueOf(i), String.valueOf(value));
			return;
		}
	}

	public void setPrepareObject(int i, Object value) throws Exception {
		if (ps == null) {
			throw new HlcException("07");
		} else {
			ps.setObject(i, value);
			mapPreparePara.put(String.valueOf(i), value);
			return;
		}
	}

	public void setPrepareString(int i, String value) throws Exception {
		if (ps == null)
			throw new HlcException("07");
		if (!cs.isSameCharset)
			value = new String(value.getBytes(cs.fileEncoding), cs.dbCharset);
		ps.setString(i, value);
		mapPreparePara.put(String.valueOf(i), value);
	}

	public static void updateConnStr(String dbName, String connStr) {
		Object aObjSet[] = mapNameConnSet.values().toArray();
		ConnSet aConnSet[] = new ConnSet[aObjSet.length];
		for (int i = 0; i < aObjSet.length; i++)
			aConnSet[i] = (ConnSet) aObjSet[i];

		for (int i = 0; i < aConnSet.length; i++) {
			ConnSet connSet = aConnSet[i];
			if (connSet.dbName.equalsIgnoreCase(dbName))
				connSet.connStr = connStr;
		}

	}

	public static synchronized void writeLog(String logType, String msg) {
		String tmpDay;
		String tmpTime;
		String nameOfTextFile;
		int flen;
		char file_content[];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		tmpDay = sdf.format(Calendar.getInstance().getTime());
		sdf = new SimpleDateFormat("HH:mm:ss");
		tmpTime = sdf.format(Calendar.getInstance().getTime());
		if (logType.equals("work"))
			logType = "Work_";
		else if (logType.equals("error"))
			logType = "Err_";
		else
			logType = "Other_";
		if (logPath.equals(""))
			logPath = ".";
		nameOfTextFile = logPath + "/" + logType + tmpDay + ".log";
		File fl = new File(nameOfTextFile);
		flen = (int) fl.length();
		file_content = new char[flen];
		try {
			if (fl.canRead()) {
				FileReader fis = new FileReader(nameOfTextFile);
				flen = fis.read(file_content);
				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			String head = "[" + tmpDay + " " + tmpTime + "]  " + msg
					+ "\r\n\r\n";
			FileWriter pw = new FileWriter(nameOfTextFile);
			pw.write(head);
			pw.write(file_content, 0, flen);
			pw.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return;
	}

	public List<Map<String, String>> queryForList(String sql) throws Exception {
		List<Map<String, String>> listMap = null;
		ResultSet rs = null;
		try {
			ResultSet rss = queryHelper(rs, sql);
			DbAccessHelper dbHelper = new DbAccessHelper();
			listMap = dbHelper.queryForList(rss);
			rss.close();
			closeStatement();
		} finally {
			if (true)
				freeConnection();
		}
		return listMap;
	}

	public Map<String, String> queryForMap(String sql) throws Exception {
		ResultSet rs = null;
		Map<String, String> listMap = null;
		try {
			ResultSet rss = queryHelper(rs, sql);
			DbAccessHelper dbHelper = new DbAccessHelper();
			listMap = dbHelper.queryForMap(rss);
			rss.close();
			closeStatement();
		} finally {
			if (true)
				freeConnection();
		}
		return listMap;
	}

	private ResultSet queryHelper(ResultSet rs, String sql) throws Exception {
		boolean isFirstTime = currentConnection == null;
		if (isFirstTime) {
			currentConnection = getConnection();
		}
		stmt = currentConnection.createStatement();
		rs = stmt.executeQuery(sql);
		return rs;
	}
}
