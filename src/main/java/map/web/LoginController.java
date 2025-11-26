package map.web;

import java.util.Base64;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import map.impl.dao.MemberDAO;
import map.security.CustomUser; // 아까 만든 UserDetails 구현체
import map.vo.MemberVO;

@Controller
public class LoginController {

    @Autowired
    private MemberDAO memberDAO;

  
    @ResponseBody
    @RequestMapping(value = "/login/googleProc.do", method = RequestMethod.POST)
    public String googleLoginProc(@RequestParam("token") String token, HttpServletRequest request) {
        
        try {
            // ==========================================
            // 1. 구글 JWT 토큰 디코딩 
            // ==========================================
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]), "UTF-8");

            // JSON 문자열 -> Map 변환
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> googleInfo = mapper.readValue(payload, Map.class);

            String email = (String) googleInfo.get("email");
            String name  = (String) googleInfo.get("name");
            String picture = (String) googleInfo.get("picture");

            // ==========================================
            // 2. DB 회원 확인 및 자동 가입
            // ==========================================
            MemberVO member = memberDAO.selectMemberByUid(email);

            if (member == null) {
                // 신규 회원이면 insert
                member = new MemberVO();
                member.setMemUid(email);
                member.setMemName(name);
                member.setMemImg(picture);
                member.setMemPw("GOOGLE_OAUTH_USER"); // 비밀번호는 더미값
                
                memberDAO.insertMember(member);
                
                // 방금 넣은 회원 정보 다시 조회 (memId 등 PK 확보)
                member = memberDAO.selectMemberByUid(email);
            } else {
                // 기존 회원이면 정보 업데이트 (프사나 이름이 바꼈을 수도 있으므로)
                // 필요하다면 update 로직 추가
            }

            // ==========================================
            // 3.  스프링 시큐리티 강제 로그인 처리 
            // ==========================================
            
            // 3-1. UserDetails 객체 생성 (우리가 만든 CustomUser)
            CustomUser customUser = new CustomUser(member);
            
            // 3-2. 인증 토큰 생성 (Credentials는 null, 권한 목록 포함)
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    customUser, 
                    null, 
                    customUser.getAuthorities()
            );
            
            // 3-3. 시큐리티 컨텍스트에 인증 객체 등록 (메모리 상 로그인)
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            // 3-4. 세션에 시큐리티 컨텍스트 저장 (중요: 그래야 페이지 이동해도 유지됨)
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            
            // (선택) JSP 등에서 쉽게 쓰기 위해 별도 세션값도 저장 (시큐리티 태그 쓰면 없어도 됨)
            session.setAttribute("memId", member.getMemId());    
            session.setAttribute("memName", member.getMemName());
            session.setAttribute("memImg", member.getMemImg());
            
            return "ok";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 로그아웃
     * spring-security.xml에 logout 설정을 했더라도,
     * 명시적인 컨트롤러 메소드가 필요할 때 사용
     */
    @RequestMapping(value = "/login/logout.do")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 삭제
        }
        // 시큐리티 컨텍스트 비우기
        SecurityContextHolder.clearContext();
        
        return "redirect:/home";
    }
}