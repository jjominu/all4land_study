package map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MapController {
	@RequestMapping(value="/oceanmap.do", method=RequestMethod.GET)
	public ModelAndView oceanmap() {
	    ModelAndView mav = new ModelAndView("map/map");
	    mav.addObject("vworldKey", System.getenv("VWORLD_KEY"));     // 또는 설정값
	    mav.addObject("safeMapKey", System.getenv("SAFEMAP_KEY"));   // 선택
	    return mav;

}
	}
