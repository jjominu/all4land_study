package map.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import map.MapService;
import map.impl.dao.ParkBookmarkDAOImpl;
import map.vo.DogParkVO;

@RestController
@RequestMapping("/api/map")
public class MapApiController {

	@Resource
	MapService ms;
	@Autowired
    private ParkBookmarkDAOImpl parkBookmarkDAO; // DAO 주입 확인

	
	@RequestMapping(value="/getDogApi.do", method=RequestMethod.GET)
	public String getDogApi() {
		System.out.println("API: "+ms.callApi());
		return ms.callApi();
	}
	
	@RequestMapping(value="/ajaxDogList.do",method=RequestMethod.POST)
	public ModelAndView ajaxDogList(DogParkVO dogParkVO) {
		
		String parkNm = dogParkVO.getParkNm();
		List<DogParkVO> dogParkList = ms.searchDogPark(dogParkVO);
		ModelAndView mav = new ModelAndView("map/fragment/dogList");
		mav.addObject("dogList",dogParkList);
		mav.addObject("parkNm",parkNm);
		return mav;
	}
	/**
     * 내 즐겨찾기 목록을 HTML(JSP)로 반환
     */
    @RequestMapping(value = "/ajaxBookmarkList.do")
    public ModelAndView ajaxBookmarkList(HttpSession session) {
        
        Integer memId = (Integer) session.getAttribute("memId");
        
       

        // 1. 내가 찜한 공원 목록 가져오기 (List<DogParkVO>)
        List<DogParkVO> bookmarkList = parkBookmarkDAO.selectMyBookmarkList(memId);
        ModelAndView mav = new ModelAndView("map/fragment/dogList");
        // 2. JSP가 사용하는 변수명("dogList")에 담기
        mav.addObject("dogList", bookmarkList);
        
        // 3. JSP 경로 (fragment/dogList.jsp)
        return mav; 
    }
	
}
