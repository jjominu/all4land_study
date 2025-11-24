package map;

import java.util.List;
import map.vo.ParkReviewVO;
import map.vo.ReviewPhotoVO;

public interface ParkReviewDAO {
    
    // 리뷰 등록
    void insertReview(ParkReviewVO vo);
    
    // 리뷰 사진 등록
    void insertReviewPhoto(ReviewPhotoVO vo);

    // 특정 공원의 리뷰 리스트 조회 (썸네일용)
    List<ParkReviewVO> selectReviewListByParkId(int parkId);

    // ★ [추가] 리뷰 단건 상세 조회 (모달용)
    ParkReviewVO selectReviewDetail(int reviewId);
}