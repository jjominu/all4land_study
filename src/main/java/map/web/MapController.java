package map.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import map.DogParkDAO;
import map.MapService;
import map.impl.dao.ParkBookmarkDAOImpl;
import map.impl.dao.ParkReviewDAO;
import map.impl.dao.ParkVisitLogDAO;
import map.vo.DogParkVO;
import map.vo.ParkBookmarkVO;
import map.vo.ParkReviewVO;
import map.vo.ParkVisitLogVO;

@Controller
@RequestMapping("/map")
public class MapController {
	@Resource
	MapService ms;
	@Autowired
    private DogParkDAO dogParkDAO;
    
    @Autowired
    private ParkReviewDAO parkReviewDAO;
    
    @Autowired
    private ParkBookmarkDAOImpl parkBookmarkDAO;
    
    @Autowired
    private ParkVisitLogDAO parkVisitLogDAO;
	
	@RequestMapping(value="/map.do", method=RequestMethod.GET)
	public ModelAndView roadMap() {
		
		List<DogParkVO> dogParkList = ms.getDogParkList();
		ModelAndView mav = new ModelAndView("map/map");
		mav.addObject("dogList",dogParkList);
		return mav;
	   
	}
	/**
     * 반려견 놀이터 상세 페이지 (ModelAndView 사용)
     * URL: /map/detail.do?id=dog_park.34
     */
    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(@RequestParam("id") String rawId, 
                               HttpServletRequest request, 
                               HttpSession session) {
        System.out.println("=========================================");
        // 1. ModelAndView 객체 생성 (이동할 JSP 경로 지정)
        // /WEB-INF/jsp/map/detail.jsp 라면 "map/detail"
        ModelAndView mv = new ModelAndView("map/detail");

        // 2. ID 파싱 ("dog_park.34" -> 34)
        int parkId = 0;
        try {
            String numStr = rawId.replaceAll("[^0-9]", "");
            parkId = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            // ID가 이상하면 지도로 리다이렉트
            mv.setViewName("redirect:/map/map.do");
            return mv;
        }

        // 3. 조회수 증가 로직 (중복 방지)
        Integer memId = (Integer) session.getAttribute("memId");
        String userIp = request.getRemoteAddr();
        
        ParkVisitLogVO logVO = new ParkVisitLogVO();
        logVO.setParkId(parkId);
        logVO.setVisitIp(userIp);
        logVO.setMemId(memId);

        int visitCount = parkVisitLogDAO.checkDuplicateVisit(logVO);
        if (visitCount == 0) {
            dogParkDAO.updateViewCount(parkId);
            parkVisitLogDAO.insertVisitLog(logVO);
        }

        // 4. 데이터 조회
        // (1) 공원 상세 정보
        DogParkVO park = dogParkDAO.selectDogParkById(parkId);
        
        // (2) 리뷰 리스트 (사진 포함)
        List<ParkReviewVO> reviewList = parkReviewDAO.selectReviewListByParkId(parkId);
        
        // (3) 즐겨찾기 여부
        boolean isBookmarked = false;
        if (memId != null) {
            ParkBookmarkVO bmVO = new ParkBookmarkVO();
            bmVO.setMemId(memId);
            bmVO.setParkId(parkId);
            int bmCount = parkBookmarkDAO.checkBookmarkStatus(bmVO);
            isBookmarked = (bmCount > 0);
        }

        // 5. 데이터 담기 (addObject)
        mv.addObject("park", park);           
        mv.addObject("reviewList", reviewList); 
        System.out.println(reviewList);
        mv.addObject("isBookmarked", isBookmarked); 
        System.out.println(park);

        return mv;
    }

	
}
