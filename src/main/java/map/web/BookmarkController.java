package map.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import map.impl.dao.ParkBookmarkDAOImpl;
import map.vo.DogParkVO;
import map.vo.ParkBookmarkVO;

@Controller
public class BookmarkController {

    @Autowired
    private ParkBookmarkDAOImpl parkBookmarkDAO;

    /**
     * 1. 즐겨찾기 토글 (추가/삭제)
     * URL: /api/bookmark/toggle.do
     */
    @ResponseBody
    @RequestMapping(value = "/api/bookmark/toggle.do", method = RequestMethod.POST)
    public String toggleBookmark(@RequestParam("parkId") int parkId, HttpSession session) {


        // 1. 로그인 체크
        Integer memId = (Integer) session.getAttribute("memId");
    	System.out.println("받은 ID 값: " + parkId); // 콘솔 확인용
    	System.out.println("받은 MEMID 값: " + memId); // 콘솔 확인용
        if (memId == null) {
            return "login_required"; // 로그인 안 했으면 에러 메시지 리턴
        }

        // 2. VO 설정
        ParkBookmarkVO vo = new ParkBookmarkVO();
        vo.setMemId(memId);
        vo.setParkId(parkId);

        // 3. 현재 상태 확인 후 처리
        int count = parkBookmarkDAO.checkBookmarkStatus(vo);

        if (count > 0) {
            // 이미 있으면 -> 삭제
            parkBookmarkDAO.deleteBookmark(vo);
            return "deleted";
        } else {
            // 없으면 -> 추가
            parkBookmarkDAO.insertBookmark(vo);
            return "inserted";
        }
    }

    /**
     * 2. 즐겨찾기 상태 확인 (팝업 열 때 아이콘 색상 결정용)
     * URL: /api/bookmark/status.do
     */
    @ResponseBody
    @RequestMapping(value = "/api/bookmark/status.do", method = RequestMethod.POST)
    public int checkStatus(@RequestParam("parkId") int parkId, HttpSession session) {
        Integer memId = (Integer) session.getAttribute("memId");
        if (memId == null) {
            return 0; // 로그인 안 했으면 즐겨찾기 안 된 상태(0)로 리턴
        }

        ParkBookmarkVO vo = new ParkBookmarkVO();
        vo.setMemId(memId);
        vo.setParkId(parkId);

        // 1이면 true(즐겨찾기 됨), 0이면 false
        return parkBookmarkDAO.checkBookmarkStatus(vo);
    }
    /**
     * 3. 내 즐겨찾기 목록 ID 가져오기
     */
    @ResponseBody
    @RequestMapping(value = "/api/bookmark/getMyList.do", method = RequestMethod.POST)
    public List<Integer> getMyBookmarkList(HttpSession session) {
        
        Integer memId = (Integer) session.getAttribute("memId");
        List<Integer> idList = new ArrayList<>();

        if (memId == null) {
            return idList; // 빈 리스트
        }

        // DAO 리턴 타입을 List<Object>가 아니라 List<DogParkVO>로 받으세요
        // (만약 DAO가 List<Object>라면 형변환만 명확하게)
        List<?> result = parkBookmarkDAO.selectMyBookmarkList(memId);
        
        for (Object obj : result) {
            // DogParkVO로 캐스팅해서 ID만 추출
            if (obj instanceof DogParkVO) {
                idList.add(((DogParkVO) obj).getId());
            }
        }
        
        System.out.println("프론트로 보낼 ID 리스트: " + idList); // 로그 확인: [1] 나오는지 확인
        return idList;
    }/**
     * 내 즐겨찾기 목록을 HTML(JSP)로 반환
     */
    @RequestMapping(value = "/ajaxBookmarkList.do")
    public String ajaxBookmarkList(HttpSession session, Model model) {
        
        Integer memId = (Integer) session.getAttribute("memId");
        
        // 로그인 안 되어 있으면 빈 목록 반환 (또는 에러 처리)
        if (memId == null) {
            return "map/fragment/dogList"; // 경로 확인 필요 (JSP 파일 위치)
        }

        // 1. 내가 찜한 공원 목록 가져오기 (List<DogParkVO>)
        // DAO 메서드 이름은 프로젝트에 맞게 확인하세요 (selectMyBookmarkList)
        List<DogParkVO> bookmarkList = parkBookmarkDAO.selectMyBookmarkList(memId);
        
        // 2. JSP가 사용하는 변수명("dogList")에 담기
        model.addAttribute("dogList", bookmarkList);
        
        // 3. 리스트만 있는 JSP 파일 경로 반환
        // (prefix/suffix 설정에 따라 .jsp 제외)
        return "map/include/ajaxDogList"; 
    }
}