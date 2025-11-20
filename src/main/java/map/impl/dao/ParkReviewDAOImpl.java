package map.impl.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import map.ParkReviewDAO;
import map.vo.ParkReviewVO;

@Repository("parkReviewDAO")
public class ParkReviewDAOImpl extends EgovAbstractMapper implements ParkReviewDAO {

    @Override
    public List<ParkReviewVO> selectReviewListByParkId(int parkId) {
        // "mappers.ParkReviewMapper"는 나중에 만들 XML의 namespace입니다.
        // "selectReviewList"는 쿼리 ID입니다.
        return selectList("mappers.ParkReviewMapper.selectReviewList", parkId);
    }
}