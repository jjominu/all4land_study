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
        
        // 1. 리뷰 본문 저장 (먼저 저장해야 review_id가 생성됨)
        parkReviewDAO.insertReview(reviewVO);
        
        // MyBatis의 selectKey/useGeneratedKeys로 채워진 ID 가져오기
        int reviewId = reviewVO.getReviewId(); 

        // 2. 파일 업로드 및 DB 저장 처리
        if (files != null && !files.isEmpty()) {
            int order = 1; // 사진 순서 (1번이 대표이미지)
            
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue; // 빈 파일 건너뜀

                // [A] 실제 파일 저장 (FileUtils 이용)
                // insertId 자리에 reviewId를 넣습니다.
                FileVO uploadedFile = FileUtils.uploadFile(file, reviewId);
                
                // [B] DB 저장을 위한 VO 변환 (FileVO -> ReviewPhotoVO)
                ReviewPhotoVO photoVO = new ReviewPhotoVO();
                photoVO.setReviewId(reviewId);
                
                // 저장된 파일명 (UUID + 원본명)
                String savedName = uploadedFile.getSaveFileName() + uploadedFile.getOriginFileName();
                photoVO.setFilePath(savedName); 
                
                photoVO.setOriginalName(uploadedFile.getOriginFileName());
                photoVO.setImgOrder(order++);

                // [C] 사진 정보 DB 저장
                parkReviewDAO.insertReviewPhoto(photoVO);
            }
        }
    }
}