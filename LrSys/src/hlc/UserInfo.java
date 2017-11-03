package hlc;
import hlc.base.db.DataSet;
import hlc.base.db.DbAccess;
import hlc.util.HlcDate;

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserInfo {

	/** 所有的id和sessionid的对应表 */
	private static Hashtable htAll = new Hashtable();
	/** 定义变量 */
	private String id; // 用户号
	private String name; // 用户名
	private String right; // 用户权限
	private String depId; // 用户机构id
	private String depName; // 用户机构名称
	private String sessionId; // SessionID
	private String chrolecode;
	private String chrolename;
	private boolean isAdmin = false; // 是否系统管理员
	private String corporation = "";
	/** 用户临时的session作用范围的数据容器，系统功能模块切换时总控可能会清空该数据容器 */
	public Hashtable htTempUserArea = new Hashtable();
	/** 定义常量 */
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	/** 存在session中的索引值 */
	private static final String TEXT_USERINFO = "user";

	/**
	 * 构造函数
	 */
	public UserInfo() {
		this.id = null;
		this.name = null;
		this.right = null;
		this.depId = null;
		this.depName = null;
	}

	/**
	 * 用户登录系统，返回是否成功的信息
	 * 
	 * @param request
	 *            就是jsp中的request对象
	 * @exception Exception
	 */
	public void login(HttpServletRequest request, String corp) throws Exception {
		DbAccess db = new DbAccess();
		DataSet ds = db.executeQuery("select * from hlc_UserOnline where chOperatorCode='"+ this.getId() + "'");
		if (!ds.isEmpty())
			throw new Exception(this.getId() + "操作员已经登录，请先解控后再登入！"); 
	
		// 查询并获得一个session
		HttpSession session = request.getSession(false);
		if (session == null) {
			session = request.getSession(true);
			while (htAll.containsValue(session.getId())) {
				// 销毁刚创建的session
				session.invalidate();
				// 创建一个新的session
				session = request.getSession(true);
				Thread.sleep(100);
			}
		}

		htAll.put(id, session.getId());
		db.executeUpdate("delete from hlc_UserOnline where chOperatorCode='"+ this.getId() + "'");
//		db.executeUpdate("insert into hlc_UserOnline(corporation,chOperatorCode,chSessionId,chLoginDate,chUserIp) values('"
//				+ corp
//				+ "','"
//				+ this.getId()
//				+ "','"
//				+ session.getId()
//				+ "','"
//				+ new HlcDate().getCurrentDate()
//				+ " "
//				+ new HlcDate().getCurrentTime()
//				+ "','"
//				+ request.getRemoteAddr() + "')");
		this.sessionId = session.getId();
	}

	/**
	 * 检查session是否有效
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @exception Exception
	 */
	public  void checkSession(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession(false);
		UserInfo user = (UserInfo) session.getAttribute(TEXT_USERINFO);
		if(htAll.get(user.getId())==null){
			throw new Exception("被非法解控，请重新登录!");
		}
		if (!(htAll.get(user.getId()).toString().equals(session.getId())))
			throw new Exception("被非法解控，请重新登录!"); //
	}

	/**
	 * 检查权限
	 * 
	 * @param sRight
	 *            用户权限串，以符号"$"分隔.
	 * @exception Exception
	 *操作员无操作权限
	 */
	public void checkRight(String sRight) throws Exception {
		String nowRight = "$" + this.right + "$";
		StringTokenizer st = new StringTokenizer(sRight, "$", false);
		while (st.hasMoreTokens()) {
			String tmp = "$" + st.nextToken() + "$";
			if (nowRight.indexOf(tmp) > -1)
				return;
		}
		throw new Exception("用户无此功能的操作权限，请和系统管理员联系！");
	}

	/**
	 * 检查权限，如果有此权限返回true，否则返回false；多个权限的只要具备其中一个就返回true
	 * 
	 * @param sRight
	 *            用户权限串，以符号"$"分隔.
	 * @return boolean 有，true；无，false
	 * @exception Exception
	 */
	public boolean hasRight(String sRight) throws Exception {
		//System.out.println("hasRight...");
		if(sRight.equals("000000000000")) return true;  //公用功能代号为12个0，不判断权限
		String nowRight = "$" + this.right + "$";
		//System.out.println("校验权限right = " + right);
		StringTokenizer st = new StringTokenizer(sRight, "$", false);
		while (st.hasMoreTokens()) {
			String tmp = "$" + st.nextToken() + "$";
			
			if (nowRight.indexOf(tmp) > -1)
				System.out.println("校验权限right = "+sRight+";true" );
				return true;
				
		}
		System.out.println("校验权限right = "+sRight+";false" );
		return false;
	}

	/**
	 * 本操作会将本session销毁，并从全局Hashtable中去掉此柜员的条目。
	 * 
	 * @param request
	 *            HttpServletRequest型的request对象
	 * @throws Exception
	 */
	public void logout(HttpServletRequest request) throws Exception {
		if (this.id != null) {
			htAll.remove(this.id);
			DbAccess db = new DbAccess();
			DataSet ds = db.executeQuery("select chSessionId from hlc_UserOnline where chOperatorCode='"+ this.getId() + "'");
			if (!ds.isEmpty() && ds.getString(0, 0).equals(this.sessionId))
				db.executeUpdate("delete from hlc_UserOnline where chOperatorCode='"+ this.getId() + "'");
		}
		HttpSession session = request.getSession();
		session.invalidate();
		this.id = null;
		this.name = null;
		this.depId = null;
		this.depName = null;
		this.right = null;
	}

	/**
	 * 解控
	 * 
	 * @param request
	 *            就是jsp中的HttpServletRequest型的request对象
	 * @param id
	 *            用户id
	 * @throws Exception
	 */
	public void setFree(HttpServletRequest request, String id) throws Exception {
		DbAccess db = new DbAccess();
		db.executeUpdate("delete from hlc_UserOnline where chOperatorCode='"+ this.getId() + "'");
		Object tmpObj = htAll.remove(id);

		HttpSession session = request.getSession();
		session.invalidate();
		this.id = null;
		this.name = null;
		this.depId = null;
		this.depName = null;
		this.right = null;
	}

	/**
	 * 检查一个柜员是否在线
	 * 
	 * @param id
	 *            柜员号
	 * @return 是否在线，ture为在线，false为不在线
	 */
	public boolean isOnNet(String id) {
		return htAll.containsKey(id);
	}

	/**
	 * 获得UserInfo实例
	 * 
	 * @param request
	 *            请求对象
	 * @return UserInfo类对象
	 * @exception Exception
	 */
	public static UserInfo getInstance(HttpServletRequest request)throws Exception {
		HttpSession session = request.getSession(false);
		Object o = session.getAttribute(TEXT_USERINFO);
		UserInfo user = (UserInfo) o;
		return user;
	}

	/**
	 * 将UserInfo实例存到session中
	 * 
	 * @param request
	 *            request对象
	 * @exception Exception
	 */
	public void save(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		session.setAttribute(TEXT_USERINFO, this);
	}

	/**
	 * 获取用户id号码
	 * 
	 * @return 用户id号码
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取用户名称
	 * 
	 * @return 用户名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取用户权限
	 * 
	 * @return 用户权限
	 */
	public String getRight() {
		return right;
	}

	/**
	 * 获取用户单位编号
	 * 
	 * @return 用户单位编号
	 */
	public String getDepId() {
		return depId;
	}

	/**
	 * 获取用户单位名称
	 * 
	 * @return 用户单位名称
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * 设置机构ID
	 * 
	 * @param depId
	 *            String 机构ID
	 */
	public void setDepId(String depId) {
		this.depId = depId;
	}

	/**
	 * 设置机构名
	 * 
	 * @param depName
	 *            String 机构名称
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            String ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 设置姓名
	 * 
	 * @param name
	 *            String 姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置权限
	 * 
	 * @param right
	 *            String 权限
	 */
	public void setRight(String right) {
		this.right = right;
	}

	/**
	 * 设置公共数据区
	 * 
	 * @param htPubArea
	 *            Hashtable
	 */
	public static void setHtPubArea(Hashtable htPubArea) {
		htPubArea = htPubArea;
	}
	/**
	 * 获取当前操作员的临时目录(服务器的绝对路径，最后包含分割符) 例 D:hlc/Data/1001/
	 * 
	 * @return String
	 */

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Object getTempUserValue(String sKey) {
		return htTempUserArea.get(sKey);
	}

	/**
	 * 将对象保存到用户临时区
	 * 
	 * @param sKey
	 *            String 参数名称
	 * @param o
	 *            Object 参数值
	 */
	public void setTempUserValue(String sKey, Object o) {
		htTempUserArea.put(sKey, o);
	}
	public String getChrolecode() {
		return chrolecode;
	}
	public void setChrolecode(String chrolecode) {
		this.chrolecode = chrolecode;
	}
	public String getChrolename() {
		return chrolename;
	}
	public void setChrolename(String chrolename) {
		this.chrolename = chrolename;
	}
	
}
