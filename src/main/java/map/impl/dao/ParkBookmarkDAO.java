package map.impl.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import map.vo.ParkBookmarkVO;

@Repository("parkBookmarkDAO")
public class ParkBookmarkDAO extends EgovAbstractMapper {

    // 즐겨찾기 추가
    public void insertBookmark(ParkBookmarkVO vo) {
        insert("mappers.BookmarkMapper.insertBookmark", vo);
    }

    // 즐겨찾기 취소
    public void deleteBookmark(ParkBookmarkVO vo) {
        delete("mappers.BookmarkMapper.deleteBookmark", vo);
    }

    // 내가 이 공원을 즐겨찾기 했는지 확인 (0이면 안함, 1이면 함)
    public int checkBookmarkStatus(ParkBookmarkVO vo) {
        return selectOne("mappers.BookmarkMapper.checkBookmarkStatus", vo);
    }
    
    // 내가 찜한 공원 목록 가져오기 (마이페이지용)
    public List<Object> selectMyBookmarkList(int memId) {
        return selectList("mappers.BookmarkMapper.selectMyBookmarkList", memId);
    }
}