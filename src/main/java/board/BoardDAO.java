package board;

import java.util.List;

import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;
import board.vo.CriteriaVO;
import board.vo.FileVO;

public interface BoardDAO {

	public List<BoardVO> getList(CriteriaVO cri);
	public void deleteBoard(int id);
	public int insertBoard(BoardVO boardVO);
	public BoardVO getDetail(int id);
	public void updateBoard(BoardVO boardVO);
	public List<BoardVO> searchBoard(BoardSearchRequestVO vo);
	public int totalBoardCnt();
	public int totalBoardCntbySearch(BoardSearchRequestVO vo);
	public void uploadFile(FileVO fileVO);
	public List<FileVO> getFile(int boardId);
	public void deleteFile(int fileId);
}
