package net.xby1993.common.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页封装类
 * 用于做分页查询的基础类，封装了一些分页的相关属性
 * @author xby taojw
 *
 */
public class Page<T> {
	public static final String ASC = "asc";
	public static final String DESC = "desc";
    // 当前页,序号从1开始
    private int pageNo=1;
 
    // 每页个个数
    private int pageSize=10;
 
    // 总条数
    private long totalCount=0;
 
    // 总页数
    private int pageCount=0;
 
    // 记录
    private List<T> results=new ArrayList<>() ;
 
    public int getPageCount() {
        return pageCount;
    }
 
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
 
 
    public List<T> getResults() {
        return results;
    }
 
    public void setResults(List<T> results) {
        this.results = results;
    }
 
    public int getPageNo() {
        return pageNo;
    }
 
    public void setPageNo(int currentPage) {
        this.pageNo = currentPage;
        if (pageNo < 1) {
			this.pageNo = 1;
		}
    }
 
    public int getPageSize() {
        return pageSize;
    }
 
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 10 : pageSize;
    }
 
    public long getTotalCount() {
        return totalCount;
    }
 
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= pageCount);
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 */
	public int getTotalPages() {
		if (totalCount < 0) {
			return -1;
		}

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return (int) count;
	}

}
