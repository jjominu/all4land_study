package board.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import board.BoardDAO;
import board.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Resource
	SqlSession sqlSession;
	
	@Override
	public List<BoardVO> getList() {
		return sqlSession.selectList("board.getList");
		
	}
	
	@Override
	public void deleteBoard(int id) {
		sqlSession.delete("board.deleteBoard",id);
		
	}
	
	@Override
	public void insertBoard(BoardVO boardVO) {
		sqlSession.insert("board.insertBoard",boardVO);
		
	}
	
	@Override
	public BoardVO getDetail(int id) {
		System.out.println("dsadsad");
		sqlSession.update("board.increaseView",id);
		return sqlSession.selectOne("board.getDetail",id); 
	}

}
