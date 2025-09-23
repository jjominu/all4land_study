package board.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import board.BoardDAO;
import board.BoardService;
import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;
import board.vo.CriteriaVO;

@Service
public class BoardServiceImpl implements BoardService{

	@Resource
	BoardDAO boardDAO;
	
	@Override
	public List<BoardVO> getList(CriteriaVO cri) {
		return boardDAO.getList(cri);
		 
	}

	@Override
	public void deleteBoard(int id) {
		boardDAO.deleteBoard(id);
		
		
	}

	@Override
	public void updateBoard(BoardVO boardVO) {
		boardDAO.updateBoard(boardVO);
		
	}

	@Override
	public void insertBoard(BoardVO boardVO) {
		System.out.println("Service :" + boardVO.getBoardId());
		boardDAO.insertBoard(boardVO);
		
	}

	@Override
	public BoardVO getDetail(int id) {
		
		return boardDAO.getDetail(id);
	}

	@Override
	public List<BoardVO> searchBoard(BoardSearchRequestVO vo) {

		return boardDAO.searchBoard(vo);
	}

	@Override
	public int totalBoardCnt() {
		
		return  boardDAO.totalBoardCnt();
	}
	

	
}
