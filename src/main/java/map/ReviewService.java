package map;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import map.vo.ParkReviewVO;

public interface ReviewService {
    void registReview(ParkReviewVO reviewVO, List<MultipartFile> files) throws Exception;
}