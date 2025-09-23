package board.vo;

public class BoardSearchRequestVO extends CriteriaVO{

	private String searchType;
	private String keyword;
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Override
	public String toString() {
		return "BoardSearchRequestVO [searchType=" + searchType + ", keyword=" + keyword +"page"+super.getPage() +"pageNum"+super.getPerPageNum()+"]";
	}

}
