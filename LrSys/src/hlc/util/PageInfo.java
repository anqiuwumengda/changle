package hlc.util;

/**
 * 分页工具类
 * @author huangjinku
 *
 */
public class PageInfo {

	/* 分页页码（用于分页） */
	private String pageNum;
	/* 每页显示记录条数 */
	private String numPerPage;
	/* 总记录数 */
	private String totalCount;
	/* 显示的页码数 */
	private String pageNumShow;
	/* 当前页码数 */
	private String currentPage;

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		if("".equals(pageNum) || pageNum==null){
			this.pageNum = "1";
		}else{
			this.pageNum = pageNum;
		}
	}

	public String getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(String numPerPage) {
		if("".equals(numPerPage) || numPerPage==null){
			this.numPerPage = "20";
		}else{
			this.numPerPage = numPerPage;
		}
		
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageNumShow() {
		return pageNumShow;
	}

	public void setPageNumShow(String pageNumShow) {
		if("".equals(pageNumShow) || pageNumShow==null){
			this.pageNumShow = "10";
		}else{
			this.pageNumShow = pageNumShow;
		}
	}

}
