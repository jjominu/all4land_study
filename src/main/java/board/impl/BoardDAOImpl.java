package board.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import board.BoardDAO;
import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;
import board.vo.CriteriaVO;
import board.vo.FileVO;

@Repository("boardDAO")
public class BoardDAOImpl extends EgovAbstractMapper implements BoardDAO {

    @Override
    public List<BoardVO> getList(CriteriaVO cri) {
        return selectList("board.getList", cri);
    }

    @Override
    public void deleteBoard(int boardId) {
        delete("board.deleteBoard", boardId);
    }

    @Override
    public int insertBoard(BoardVO boardVO) {

        return insert("board.insertBoard", boardVO);
    }

    @Override
    public BoardVO getDetail(int boardId) {
        // 조회수 증가
        update("board.increaseView", boardId);
        // 상세 조회
        return selectOne("board.getDetail", boardId);
    }

    @Override
    public void updateBoard(BoardVO boardVO) {
        update("board.updateBoard", boardVO);
    }

    @Override
    public List<BoardVO> searchBoard(BoardSearchRequestVO vo) {
        return selectList("board.searchBoard", vo);
    }

    @Override
    public int totalBoardCnt() {
        return selectOne("board.totalBoardCnt");
    }

    @Override
    public int totalBoardCntbySearch(BoardSearchRequestVO vo) {
        return selectOne("board.totalBoardCntbySearch", vo);
    }

    @Override
    public void uploadFile(FileVO fileVO) {
        insert("board.uploadFile", fileVO);
    }

    @Override
    public List<FileVO> getFile(int boardId) {
        return selectList("board.getFiles", boardId);
    }

    @Override
    public void deleteFile(int fileId) {
        delete("board.deleteFile", fileId);
    }

    @Override
    public BoardVO nextBoard(int id) {
        return selectOne("board.nextBoard", id);
    }

    @Override
    public BoardVO previousBoard(int boardId) {
        return selectOne("board.previousBoard", boardId);
    }

    @Override
    public FileVO getFileById(int fileId) {
        return selectOne("board.getFileById", fileId);
    }
}
