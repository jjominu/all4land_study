package map.impl.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import map.DogParkDAO;
import map.MapService;
import map.vo.DogParkVO;
@Service
public class MapServiceImpl implements MapService {

    @Resource
	RestTemplate restTemplate;
    @Resource
    DogParkDAO dogParkDAO;

   

	// 외부 API 호출 메서드
    public String callApi() {

        //  vWorld API URL
        String url = "http://localhost:9090/geoserver/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=DogPark:dog_park&outputFormat=application/json\r\n"
        		+ "";

        //  헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //  HttpEntity 생성 (GET 요청이므로 body는 null)
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        
        return response.getBody();
    }
    
    public List<DogParkVO> getDogParkList() {
    	return dogParkDAO.findAll();
    }

	@Override
	public List<DogParkVO> searchDogPark(DogParkVO DogParkVO) {
		
		return dogParkDAO.findDogParksByFilter(DogParkVO);
	}
//	public DogParkVO getParkDetail(int parkId, String userIp, Integer memId) {
//	    
//	    // 1. 로그용 VO 생성
//	    ParkVisitLogVO logVO = new ParkVisitLogVO();
//	    logVO.setParkId(parkId);
//	    logVO.setVisitIp(userIp);
//	    logVO.setMemId(memId); // 로그인 안했으면 null
//
//	    // 2. 오늘 이미 방문했는지 체크
//	    int visitCount = parkVisitLogDAO.checkDuplicateVisit(logVO);
//
//	    if (visitCount == 0) {
//	        // 3. 오늘 첫 방문이면 -> 조회수 증가 + 로그 저장
//	        dogParkDAO.updateViewCount(parkId); // 조회수 +1
//	        parkVisitLogDAO.insertVisitLog(logVO); // 로그 기록
//	    }
//
//	    // 4. 공원 정보 리턴
//	    return dogParkDAO.selectDogParkById(parkId);
//	}
//    
   
}
