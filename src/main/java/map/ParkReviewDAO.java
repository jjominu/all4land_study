package map;

import java.util.List;
import map.vo.ParkReviewVO;

public interface ParkReviewDAO {
    // 특정 공원의 리뷰 목록 가져오기
    List<ParkReviewVO> selectReviewListByParkId(int parkId);
}