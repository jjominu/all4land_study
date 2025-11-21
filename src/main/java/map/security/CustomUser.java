package map.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import map.vo.MemberVO;

// 스프링 시큐리티의 User 클래스를 상속받음
public class CustomUser extends User {
    
    private MemberVO member; // 우리의 실제 회원 정보

    public CustomUser(MemberVO member) {
        // 아이디, 패스워드, 권한리스트를 부모 생성자에 넘김
        super(member.getMemUid(), member.getMemPw(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }

    public MemberVO getMember() { return member; }
}