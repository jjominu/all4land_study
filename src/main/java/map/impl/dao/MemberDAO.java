package map.impl.dao;

import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import map.vo.MemberVO;

@Repository("memberDAO")
public class MemberDAO extends EgovAbstractMapper {

    // 아이디로 회원 정보 조회 (로그인용)
    public MemberVO selectMemberByUid(String memUid) {
        return selectOne("mappers.MemberMapper.selectMemberByUid", memUid);
    }

    // 회원가입
    public void insertMember(MemberVO vo) {
        insert("mappers.MemberMapper.insertMember", vo);
    }
}