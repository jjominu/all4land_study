package map;

import java.util.List;
import map.vo.ParkReviewVO;
import map.vo.ReviewPhotoVO;

public interface ParkReviewDAO {
    
    void insertReview(ParkReviewVO vo);
    
    void insertReviewPhoto(ReviewPhotoVO vo);

    List<ParkReviewVO> selectReviewListByParkId(int parkId);

    ParkReviewVO selectReviewDetail(int reviewId);
}