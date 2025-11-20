package map.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

// Lombok을 쓰신다면 @Data 추가
public class ParkReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // [1] park_review 테이블 컬럼
    private Integer reviewId;       // review_id
    private Integer parkId;         // park_id
    private Integer memId;          // mem_id
    private String reviewContent;   // review_content
    private Integer rating;         // rating
    private Integer likeCount;      // like_count
    private Date createdAt;         // created_at
    private Date updatedAt;         // updated_at

    // [2] member 테이블 조인용 (작성자 정보)
    private String memName;         // 작성자 닉네임
    private String memImg;          // 작성자 프로필 이미지

    // [3] ★ 1:N 관계 매핑용 (사진 리스트) ★
    // 나중에 xml 쿼리(resultMap)에서 이 변수명(photoList)을 사용합니다.
    private List<ReviewPhotoVO> photoList;


    /* Getter / Setter 생략 (Lombok 미사용 시 직접 생성해주세요) */
    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }

    public Integer getParkId() { return parkId; }
    public void setParkId(Integer parkId) { this.parkId = parkId; }

    public Integer getMemId() { return memId; }
    public void setMemId(Integer memId) { this.memId = memId; }

    public String getReviewContent() { return reviewContent; }
    public void setReviewContent(String reviewContent) { this.reviewContent = reviewContent; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getMemName() { return memName; }
    public void setMemName(String memName) { this.memName = memName; }

    public String getMemImg() { return memImg; }
    public void setMemImg(String memImg) { this.memImg = memImg; }

    // ★ 핵심 Getter/Setter
    public List<ReviewPhotoVO> getPhotoList() { return photoList; }
    public void setPhotoList(List<ReviewPhotoVO> photoList) { this.photoList = photoList; }
}