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

    public void insertReviewPhoto(ReviewPhotoVO vo) {
        insert("mappers.ParkReviewMapper.insertReviewPhoto", vo);
    }

   
    public List<ParkReviewVO> selectReviewListByParkId(int parkId) {
        return selectList("mappers.ParkReviewMapper.selectReviewList", parkId);
    }
    
    public ParkReviewVO selectReviewDetail(int reviewId) {
        return selectOne("mappers.ParkReviewMapper.selectReviewDetail", reviewId);
    }
}