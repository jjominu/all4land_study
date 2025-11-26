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
            Integer memId = (Integer) session.getAttribute("memId");
            if (memId == null) {
                return "login_required";
            }
            reviewVO.setMemId(memId);

            List<MultipartFile> files = request.getFiles("uploadFiles");

            List<String> errors = FileUtils.validateFiles(files.toArray(new MultipartFile[0]));
            if (!errors.isEmpty()) {
                return "file_error: " + errors.get(0);
            }
            reviewService.registReview(reviewVO, files);

            return "ok";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.getMessage();
        }
    }
    
   
    @ResponseBody
    @RequestMapping(value = "/review/detail.do", method = RequestMethod.POST)
    public ParkReviewVO getReviewDetail(@RequestParam("reviewId") int reviewId) {
        return parkReviewDAO.selectReviewDetail(reviewId);
    }
}