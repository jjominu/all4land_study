package board.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import board.BoardDAO;
import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;
import board.vo.CriteriaVO;
import board.vo.FileVO;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Resource
	SqlSession sqlSession;
	
	@Override
	public List<BoardVO> getList(CriteriaVO cri) {
		return sqlSession.selectList("board.getList",cri);
		
	}
	
	@Override
	public void deleteBoard(int boardId) {
		sqlSession.delete("board.deleteBoard",boardId);
		
	}
	
	@Override
	public int insertBoard(BoardVO boardVO) {
		return sqlSession.selectOne("board.insertBoard",boardVO);
		
	}
	
	@Override
	public BoardVO getDetail(int boardId) {
		System.out.println(boardId);
		
		sqlSession.update("board.increaseView",boardId);
		return sqlSession.selectOne("board.getDetail",boardId); 
	}

	@Override
	public void updateBoard(BoardVO boardVO) {
		sqlSession.update("board.updateBoard", boardVO);
		
	}

	@Override
	public List<BoardVO> searchBoard(BoardSearchRequestVO vo) {
		List<BoardVO> ls = sqlSession.selectList("board.searchBoard",vo);
		return ls;
	}

	@Override
	public int totalBoardCnt() {
		
		return sqlSession.selectOne("board.totalBoardCnt");
	}

	@Override
	public int totalBoardCntbySearch(BoardSearchRequestVO vo) {
		
		return  sqlSession.selectOne("board.totalBoardCntbySearch",vo);
	}

	@Override
	public void uploadFile(FileVO fileVO) {

		sqlSession.insert("board.uploadFile",fileVO);
	}

	@Override
	public List<FileVO> getFile(int boardId) {
		return sqlSession.selectList("board.getFiles",boardId);
	}

	@Override
	public void deleteFile(int fileId) {

		sqlSession.delete("board.deleteFile",fileId);
	}

	@Override
	public  BoardVO nextBoard(int id) {
		return sqlSession.selectOne("board.nextBoard",id);
	}

	@Override
	public BoardVO previousBoard(int boardId) {
		
		return sqlSession.selectOne("board.previousBoard",boardId);
	}
	
	

}
