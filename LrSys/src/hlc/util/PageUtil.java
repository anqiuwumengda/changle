package hlc.util;

public class PageUtil {

	public static int getPageNumShow(int totalCount, int inumPerPage){
		int pageNumShown = (totalCount+inumPerPage-1)/inumPerPage;
		pageNumShown = pageNumShown > 10 ? 10 : pageNumShown;
		return pageNumShown;
	}
}
