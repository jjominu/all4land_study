package map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MapController {

	    @RequestMapping("/map.do")
	    public ModelAndView map() {
	    	System.out.println("Dsadas");
	        ModelAndView mav = new ModelAndView("map/map"); // /WEB-INF/views/map.jsp
	        return mav;
	    }
	
}
