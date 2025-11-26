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
	
    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(@RequestParam("id") String rawId, 
                               HttpServletRequest request, 
                               HttpSession session) {
        
        ModelAndView mv = new ModelAndView("map/detail");

        int parkId = 0;
        try {
            String numStr = rawId.replaceAll("[^0-9]", "");
            parkId = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            mv.setViewName("redirect:/map/map.do");
            return mv;
        }

        Integer memId = (Integer) session.getAttribute("memId");
        String userIp = request.getRemoteAddr();
        
        ParkVisitLogVO logVO = new ParkVisitLogVO();
        logVO.setParkId(parkId);
        logVO.setVisitIp(userIp);
        logVO.setMemId(memId);

		/*
		 * int visitCount = parkVisitLogDAO.checkDuplicateVisit(logVO); if (visitCount
		 * == 0) { dogParkDAO.updateViewCount(parkId);
		 * parkVisitLogDAO.insertVisitLog(logVO); }
		 */
        
        dogParkDAO.updateViewCount(parkId);
        parkVisitLogDAO.insertVisitLog(logVO);

        
        DogParkVO park = dogParkDAO.selectDogParkById(parkId);
        
        List<ParkReviewVO> reviewList = parkReviewDAO.selectReviewListByParkId(parkId);
        
        boolean isBookmarked = false;
        if (memId != null) {
            ParkBookmarkVO bmVO = new ParkBookmarkVO();
            bmVO.setMemId(memId);
            bmVO.setParkId(parkId);
            int bmCount = parkBookmarkDAO.checkBookmarkStatus(bmVO);
            isBookmarked = (bmCount > 0);
        }

        int bookmarkCount = parkBookmarkDAO.countBookmarkByParkId(parkId);

        mv.addObject("park", park);           
        mv.addObject("reviewList", reviewList); 
        mv.addObject("isBookmarked", isBookmarked); 
        mv.addObject("bookmarkCount", bookmarkCount); // ★ JSP로 전달

        return mv;
    }

	
}
