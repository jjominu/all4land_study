package map.impl;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import map.MapService;
@Service
public class MapServiceImpl implements MapService {

    @Resource
	RestTemplate restTemplate;

   

	// 외부 API 호출 메서드
    public String callApi() {
        // ✅ vWorld API URL
        String url = "https://api.vworld.kr/req/data?" +
                "service=data&version=2.0&request=GetFeature&format=json&errorformat=json" +
                "&data=LT_C_DOGPARK" +
                "&geometry=true&attribute=true" +
                "&columns=sd_nm,sgg_nm,park_nm,oper_tm,hldy,fcs,fcar,oper_inst,telno,addr,use_amt,ag_geom" +
                "&size=1000&page=1&crs=EPSG:3857" +
                "&geomfilter=BOX(13803616.8584,3895303.9634,14694172.7847,4721671.5726)" +
                "&key=17C7EB57-45CC-3193-9DB9-AADAC973D076" +
                "&domain=http://localhost:8080";

        // ✅ 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ HttpEntity 생성 (GET 요청이므로 body는 null)
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // ✅ GET 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        
        return response.getBody();
    }
}
