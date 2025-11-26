package map;

import java.util.List;

import map.vo.DogParkVO;
import map.vo.ParkBookmarkVO;

public interface ParkBookmarkDAO {
   void insertBookmark(ParkBookmarkVO vo) ;
     void deleteBookmark(ParkBookmarkVO vo) ;

     int checkBookmarkStatus(ParkBookmarkVO vo) ;
    
     List<DogParkVO> selectMyBookmarkList(int memId) ;
     }
