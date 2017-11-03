package com.tgb.page;

import java.util.Map;

public class PageUtil {
	public static PageContext isPage(Map map) {
		PageContext page = null;
		if (null != map && !map.isEmpty()) {
			Object obj = map.get("isPagination");
			if (null != obj && !"".equals(obj) && "true".equals(obj.toString())) {
				page = PageContext.getContext();
				page.setPagination(true);
				Object currPage = map.get("currentPage");
				Object pageSize = map.get("pageSize");
				Object totalPages = map.get("totalPages");
				if (null != totalPages && !"".equals(totalPages.toString())) {
					page.setCurrentPage(Integer.parseInt(totalPages.toString()));
					map.remove("totalPages");
				}
				if (null != currPage && !"".equals(currPage.toString())) {
					page.setCurrentPage(Integer.parseInt(currPage.toString()));
					map.remove("currentPage");
				}
				if (null != pageSize && !"".equals(pageSize.toString())) {
					page.setPageSize(Integer.parseInt(pageSize.toString()));
					map.remove("pageSize");
				}
				map.put("page", page);
				map.remove("isPagination");
				return page;
			} else
				return page;
		} else {
			return page;
		}
	}

}
