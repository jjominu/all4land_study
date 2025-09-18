package board;

import java.util.List;

public interface BoardService {

	public List<BoardVO> getList();
	public void deleteBoard(int id);
	public void insertBoard(BoardVO boardVO);
	public BoardVO getDetail(int id);
}
