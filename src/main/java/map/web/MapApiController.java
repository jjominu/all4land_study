package map.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import map.MapService;
import map.vo.DogParkVO;

@RestController
@RequestMapping("/api/map")
public class MapApiController {

	@Resource
	MapService ms;
	

	
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
	
}
