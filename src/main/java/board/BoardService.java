package board;

import java.util.List;

import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;
import board.vo.CriteriaVO;

public interface BoardService {

	public List<BoardVO> getList(CriteriaVO cri);
	public void deleteBoard(int id);
	public void insertBoard(BoardVO boardVO);
	public BoardVO getDetail(int id);
	public void updateBoard(BoardVO boardVO);
	public List<BoardVO> searchBoard(BoardSearchRequestVO vo);
	public int totalBoardCnt();
	public int totalBoardCntbySearch(BoardSearchRequestVO vo);
}
