package map.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import map.MapService;
import map.vo.DogParkVO;

@Controller
@RequestMapping("/map")
public class MapController {
	@Resource
	MapService ms;
	
	@RequestMapping(value="/map.do", method=RequestMethod.GET)
	public ModelAndView roadMap() {
		
		List<DogParkVO> dogParkList = ms.getDogParkList();
		ModelAndView mav = new ModelAndView("map/map");
		mav.addObject("dogList",dogParkList);
		return mav;
	   
	}
	
	@RequestMapping(value="/getParkByParkName.do",method=RequestMethod.GET)
	public ModelAndView getParkByParkName( @RequestParam(value="park_nm", required=false) String parkNm) {
		List<DogParkVO> dogParkList = ms.getParkByParkName(parkNm);
		ModelAndView mav = new ModelAndView("map/map");
		mav.addObject("dogList",dogParkList);
		return mav;
	}
	
}
