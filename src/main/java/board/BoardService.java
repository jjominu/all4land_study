package board;

import java.util.List;

import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;

public interface BoardService {

	public List<BoardVO> getList();
	public void deleteBoard(int id);
	public void insertBoard(BoardVO boardVO);
	public BoardVO getDetail(int id);
	public void updateBoard(BoardVO boardVO);
	public List<BoardVO> searchBoard(BoardSearchRequestVO vo);
}
