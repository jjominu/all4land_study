package map.impl.dao;

import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import map.vo.MemberVO;

@Repository("memberDAO")
public class MemberDAO extends EgovAbstractMapper {

    public MemberVO selectMemberByUid(String memUid) {
        return selectOne("mappers.MemberMapper.selectMemberByUid", memUid);
    }

    
    public void insertMember(MemberVO vo) {
        insert("mappers.MemberMapper.insertMember", vo);
    }
}