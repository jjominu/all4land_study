package map.impl.dao;

import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import map.vo.ParkVisitLogVO;

@Repository("parkVisitLogDAO")
public class ParkVisitLogDAO extends EgovAbstractMapper {

    // 1. 방문 기록 저장 (상세페이지 접속 시 호출)
    public void insertVisitLog(ParkVisitLogVO vo) {
        insert("mappers.VisitLogMapper.insertVisitLog", vo);
    }

    // 2. 동일 IP가 오늘 해당 공원을 방문했는지 체크 (조회수 중복 방지용)
    // 리턴값: 0이면 첫방문, 1이상이면 재방문
    public int checkDuplicateVisit(ParkVisitLogVO vo) {
        return selectOne("mappers.VisitLogMapper.checkDuplicateVisit", vo);
    }

    // (선택) 특정 공원의 오늘 방문자 수
    public int countTodayVisit(int parkId) {
        return selectOne("mappers.VisitLogMapper.countTodayVisit", parkId);
    }
}