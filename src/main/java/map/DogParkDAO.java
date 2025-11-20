package map;

import java.util.List;

import map.vo.DogParkVO;

public interface DogParkDAO {


    List<DogParkVO> findAll();

    List<DogParkVO> findDogParksByFilter(DogParkVO DogParkVO);
}
