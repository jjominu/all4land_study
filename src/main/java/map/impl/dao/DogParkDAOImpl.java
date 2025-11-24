package map.impl.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import map.DogParkDAO;
import map.vo.DogParkVO;

@Repository("dogParkDAO")
public class DogParkDAOImpl extends EgovAbstractMapper implements DogParkDAO {

    @Override
    public List<DogParkVO> findAll() {
        List<DogParkVO> list = selectList("mappers.DogParkMapper.selectDogParkList");
        return list;
    }

    @Override
    public List<DogParkVO> findDogParksByFilter(DogParkVO DogParkVO) {
    	List<DogParkVO> list =selectList("mappers.DogParkMapper.searchDogParkList", DogParkVO);
    	System.out.println(list);
        return list ;
    }
    
 // 기존 클래스 내부에 추가
    @Override
    public void updateViewCount(int id) {
        update("mappers.DogParkMapper.updateViewCount", id);
    }
    @Override
    // 상세 조회 (기존에 없다면 추가)
    public DogParkVO selectDogParkById(int id) {
    	System.out.println("DAO========================="+selectOne("mappers.DogParkMapper.selectDogParkById", id));
        return selectOne("mappers.DogParkMapper.selectDogParkById", id);
    }
}
