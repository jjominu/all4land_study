package common;

import board.vo.CriteriaVO;

public class PlacticePageUtil {
	
	private CriteriaVO cri;
	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prevPage;
	private boolean nextPage;
	private int displayPageNum = 10;
	public CriteriaVO getCri() {
		return cri;
	}
	public void setCri(CriteriaVO cri) {
		this.cri = cri;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	
	public void  calcData() {
		endPage = (int)(Math.ceil(cri.getPage()/(double)displayPageNum)*displayPageNum);
		// 마지막 페이지 = 현재페이지 / 보여질 페이지 갯수 -> ex) 1/10 = 0.1 ->Math.ceil(0.1) -> 1 *10 = 10 
		//                                        ex) 12/10= 1.2 -?Math.ceil(1.2) -> 2*10 = 20
		startPage = endPage - displayPageNum +1;
		
		
		
		
	}
	public boolean isPrevPage() {
		return prevPage;
	}
	public void setPrevPage(boolean prevPage) {
		this.prevPage = prevPage;
	}
	public boolean isNextPage() {
		return nextPage;
	}
	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}
	public int getDisplayPageNum() {
		return displayPageNum;
	}
	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}
	
	

}
