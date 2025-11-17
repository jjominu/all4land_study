package map.impl;

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
    public DogParkVO findById(int id) {
        return selectOne("mappers.DogParkMapper.selectDogParkById", id);
    }
}
