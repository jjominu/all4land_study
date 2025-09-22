package board.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import board.BoardDAO;
import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Resource
	SqlSession sqlSession;
	
	@Override
	public List<BoardVO> getList() {
		return sqlSession.selectList("board.getList");
		
	}
	
	@Override
	public void deleteBoard(int boardId) {
		sqlSession.delete("board.deleteBoard",boardId);
		
	}
	
	@Override
	public void insertBoard(BoardVO boardVO) {
		sqlSession.insert("board.insertBoard",boardVO);
		
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
		List<BoardVO> ls = sqlSession.selectList("board.searchBoard", vo);
		return ls;
	}

}
