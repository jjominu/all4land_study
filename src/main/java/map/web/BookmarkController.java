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

  
    @ResponseBody
    @RequestMapping(value = "/api/bookmark/toggle.do", method = RequestMethod.POST)
    public String toggleBookmark(@RequestParam("parkId") int parkId, HttpSession session) {


        Integer memId = (Integer) session.getAttribute("memId");
    	System.out.println("받은 ID 값: " + parkId); 
    	System.out.println("받은 MEMID 값: " + memId); 
        if (memId == null) {
            return "login_required";
        }

        ParkBookmarkVO vo = new ParkBookmarkVO();
        vo.setMemId(memId);
        vo.setParkId(parkId);
        
        int count = parkBookmarkDAO.checkBookmarkStatus(vo);

        if (count > 0) {
            parkBookmarkDAO.deleteBookmark(vo);
            return "deleted";
        } else {
            parkBookmarkDAO.insertBookmark(vo);
            return "inserted";
        }
    }

  
    @ResponseBody
    @RequestMapping(value = "/api/bookmark/status.do", method = RequestMethod.POST)
    public int checkStatus(@RequestParam("parkId") int parkId, HttpSession session) {
        Integer memId = (Integer) session.getAttribute("memId");
        if (memId == null) {
            return 0; 
        }

        ParkBookmarkVO vo = new ParkBookmarkVO();
        vo.setMemId(memId);
        vo.setParkId(parkId);

        return parkBookmarkDAO.checkBookmarkStatus(vo);
    }
   
    @ResponseBody
    @RequestMapping(value = "/api/bookmark/getMyList.do", method = RequestMethod.POST)
    public List<Integer> getMyBookmarkList(HttpSession session) {
        
        Integer memId = (Integer) session.getAttribute("memId");
        List<Integer> idList = new ArrayList<>();

        if (memId == null) {
            return idList; 
        }

       
        List<?> result = parkBookmarkDAO.selectMyBookmarkList(memId);
        
        for (Object obj : result) {
            if (obj instanceof DogParkVO) {
                idList.add(((DogParkVO) obj).getId());
            }
        }
        
        System.out.println("프론트로 보낼 ID 리스트: " + idList); 
        return idList;
    }
    @RequestMapping(value = "/ajaxBookmarkList.do")
    public String ajaxBookmarkList(HttpSession session, Model model) {
        
        Integer memId = (Integer) session.getAttribute("memId");
        
        if (memId == null) {
            return "map/fragment/dogList"; 
        }

       
        List<DogParkVO> bookmarkList = parkBookmarkDAO.selectMyBookmarkList(memId);
        
        model.addAttribute("dogList", bookmarkList);
      
        return "map/include/ajaxDogList"; 
    }
}