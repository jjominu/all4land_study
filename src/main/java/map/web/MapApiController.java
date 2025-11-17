package map.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import map.MapService;

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
}
