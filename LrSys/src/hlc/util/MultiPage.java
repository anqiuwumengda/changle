package hlc.util;

import java.util.HashMap;
import java.util.Map;

import hlc.base.EnBase;

public class MultiPage {
	//有页面传入
	private int numPerPage = 20; //每页显示的记录数
	private int pageNum; //当前页码
	private int totalCount = 0; //总记录数
	private int pageNumShown = 10;//显示的页数
	private EnBase[] enArray = new EnBase[0]; //对象数组名
	private Map<Object,Object> pageData = new HashMap<Object,Object>();
	

	public MultiPage(){
		
	}
	public MultiPage(int pageNum,int numPerPage,int totalCount){
		if(pageNum==0){
			pageNum=1;
		}
		this.pageNum = pageNum;
		if(numPerPage==0){
			numPerPage=20;
		}
		this.numPerPage = numPerPage;
		this.totalCount = totalCount;
		
		
	}
	public int getPageNumShow() {
		int pageNumShown = (totalCount + numPerPage - 1) / numPerPage;
		pageNumShown = pageNumShown > 10 ? 10 : pageNumShown;
		return pageNumShown;
	}
	public int getPageCount() {
		int pageNumShown = (totalCount + numPerPage - 1) / numPerPage;
		//pageNumShown = pageNumShown > 10 ? 10 : pageNumShown;
		return pageNumShown;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		if(numPerPage==0){
			numPerPage=20;
		}
		this.numPerPage = numPerPage;
	}

	

	public int getPageNumShown() {
		return pageNumShown;
	}

	public void setPageNumShown(int pageNumShown) {
		this.pageNumShown = pageNumShown;
	}


	public EnBase[] getEnArray() {
		return enArray;
	}

	public void setEnArray(EnBase[] enArray) {
		this.enArray = enArray;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		if(pageNum==0){
			pageNum=1;
		}
		this.pageNum = pageNum;
	}
	public Map<Object,Object> getPageData() {
		return pageData;
	}
	public void setPageData(Map<Object,Object> pageData) {
		this.pageData = pageData;
	}
	public int getPagecount() {
		return (pageNum-1)*numPerPage;
	}
}
