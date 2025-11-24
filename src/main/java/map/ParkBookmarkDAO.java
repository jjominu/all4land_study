package map;

import java.util.List;

import map.vo.DogParkVO;
import map.vo.ParkBookmarkVO;

public interface ParkBookmarkDAO {
	// 즐겨찾기 추가
   void insertBookmark(ParkBookmarkVO vo) ;
    // 즐겨찾기 취소
     void deleteBookmark(ParkBookmarkVO vo) ;

    // 내가 이 공원을 즐겨찾기 했는지 확인 (0이면 안함, 1이면 함)
     int checkBookmarkStatus(ParkBookmarkVO vo) ;
    
    // 내가 찜한 공원 목록 가져오기 (마이페이지용)
     List<DogParkVO> selectMyBookmarkList(int memId) ;
     }
