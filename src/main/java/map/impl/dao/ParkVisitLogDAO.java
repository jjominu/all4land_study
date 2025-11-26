package map.impl.dao;

import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import map.vo.ParkVisitLogVO;

@Repository("parkVisitLogDAO")
public class ParkVisitLogDAO extends EgovAbstractMapper {

    public void insertVisitLog(ParkVisitLogVO vo) {
        insert("mappers.VisitLogMapper.insertVisitLog", vo);
    }


    public int checkDuplicateVisit(ParkVisitLogVO vo) {
        return selectOne("mappers.VisitLogMapper.checkDuplicateVisit", vo);
    }

    public int countTodayVisit(int parkId) {
        return selectOne("mappers.VisitLogMapper.countTodayVisit", parkId);
    }
}