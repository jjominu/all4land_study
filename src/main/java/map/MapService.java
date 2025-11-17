package map;

import java.util.List;

import map.vo.DogParkVO;

public interface MapService {

	public String callApi();
	public List<DogParkVO> getDogParkList();
	
}
