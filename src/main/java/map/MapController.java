package map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/map")
public class MapController {
	@RequestMapping(value="/map.do", method=RequestMethod.GET)
	public ModelAndView roadMap() {
		
		ModelAndView mav = new ModelAndView("map/test");
		return mav;
	   

}
	}
