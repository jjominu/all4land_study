package map.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import common.FileUtils;
import map.ReviewService;
import map.impl.dao.ParkReviewDAO;
import map.vo.ParkReviewVO;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ParkReviewDAO parkReviewDAO;

    @ResponseBody
    @RequestMapping(value = "/review/add.do", method = RequestMethod.POST)
    public String addReview(ParkReviewVO reviewVO, 
                            MultipartHttpServletRequest request, 
                            HttpSession session) {
        try {
            // 1. 로그인 체크
            Integer memId = (Integer) session.getAttribute("memId");
            if (memId == null) {
                return "login_required";
            }
            reviewVO.setMemId(memId);

            // 2. 파일 가져오기
            List<MultipartFile> files = request.getFiles("uploadFiles");

            // 3. 파일 유효성 검사 (FileUtils 활용)
            // List<MultipartFile>을 배열로 변환하여 전달
            List<String> errors = FileUtils.validateFiles(files.toArray(new MultipartFile[0]));
            if (!errors.isEmpty()) {
                // 에러 메시지 중 첫 번째만 리턴 (혹은 JSON으로 다 보내도 됨)
                return "file_error: " + errors.get(0);
            }

            // 4. 서비스 호출 (글 저장 + 파일 저장 트랜잭션)
            reviewService.registReview(reviewVO, files);

            return "ok";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.getMessage();
        }
    }
    
    /**
     * 리뷰 상세 정보 가져오기 (Modal용 AJAX)
     */
    @ResponseBody
    @RequestMapping(value = "/review/detail.do", method = RequestMethod.POST)
    public ParkReviewVO getReviewDetail(@RequestParam("reviewId") int reviewId) {
        return parkReviewDAO.selectReviewDetail(reviewId);
    }
}