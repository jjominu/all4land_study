package map.impl.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import map.vo.ParkReviewVO;
import map.vo.ReviewPhotoVO;

@Repository("parkReviewDAO")
public class ParkReviewDAO extends EgovAbstractMapper {

    public void insertReview(ParkReviewVO vo) {
        insert("mappers.ParkReviewMapper.insertReview", vo);
    }

    // ★ [추가] 사진 등록 구현
    public void insertReviewPhoto(ReviewPhotoVO vo) {
        // ParkReview_SQL.xml에 정의된 insert id와 일치해야 함
        insert("mappers.ParkReviewMapper.insertReviewPhoto", vo);
    }

   
    public List<ParkReviewVO> selectReviewListByParkId(int parkId) {
        return selectList("mappers.ParkReviewMapper.selectReviewList", parkId);
    }
  // ★ [추가] 상세 조회 구현
    
    public ParkReviewVO selectReviewDetail(int reviewId) {
        // ParkReview_SQL.xml에 정의된 select id 호출
        return selectOne("mappers.ParkReviewMapper.selectReviewDetail", reviewId);
    }
}