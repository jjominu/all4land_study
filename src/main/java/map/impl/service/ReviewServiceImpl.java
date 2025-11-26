package map.impl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import board.vo.FileVO; // FileUtils가 반환하는 VO
import common.FileUtils; // 제공해주신 파일 유틸
import map.ReviewService;
import map.impl.dao.ParkReviewDAO;
import map.vo.ParkReviewVO;
import map.vo.ReviewPhotoVO;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ParkReviewDAO parkReviewDAO;

    @Override
    public void registReview(ParkReviewVO reviewVO, List<MultipartFile> files) throws Exception {
        
        parkReviewDAO.insertReview(reviewVO);
        
        int reviewId = reviewVO.getReviewId(); 

        //  파일 업로드 및 DB 저장 처리
        if (files != null && !files.isEmpty()) {
            int order = 1; // 사진 순서 1번이 대표이미지
            
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue; 

                FileVO uploadedFile = FileUtils.uploadFile(file, reviewId);
                
                ReviewPhotoVO photoVO = new ReviewPhotoVO();
                photoVO.setReviewId(reviewId);
                
                String savedName = uploadedFile.getSaveFileName() + uploadedFile.getOriginFileName();
                photoVO.setFilePath(savedName); 
                
                photoVO.setOriginalName(uploadedFile.getOriginFileName());
                photoVO.setImgOrder(order++);

                parkReviewDAO.insertReviewPhoto(photoVO);
            }
        }
    }
}