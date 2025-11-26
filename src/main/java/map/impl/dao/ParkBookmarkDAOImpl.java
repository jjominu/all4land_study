package map.impl.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import map.ParkBookmarkDAO;
import map.vo.DogParkVO;
import map.vo.ParkBookmarkVO;

@Repository("parkBookmarkDAO")
public class ParkBookmarkDAOImpl extends EgovAbstractMapper implements ParkBookmarkDAO{

    public void insertBookmark(ParkBookmarkVO vo) {
        insert("mappers.BookmarkMapper.insertBookmark", vo);
    }

    public void deleteBookmark(ParkBookmarkVO vo) {
        delete("mappers.BookmarkMapper.deleteBookmark", vo);
    }

    public int checkBookmarkStatus(ParkBookmarkVO vo) {
        return selectOne("mappers.BookmarkMapper.checkBookmarkStatus", vo);
    }
    
    public List<DogParkVO> selectMyBookmarkList(int memId) {
        return selectList("mappers.BookmarkMapper.selectMyBookmarkList", memId);
    }
    
    public int countBookmarkByParkId(int parkId) {
        return selectOne("mappers.BookmarkMapper.countBookmarkByParkId", parkId);
    }
}