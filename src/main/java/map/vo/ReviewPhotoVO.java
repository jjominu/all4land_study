package map.vo;

import java.io.Serializable;

// Lombok을 쓰신다면 @Data 추가, 안 쓰신다면 아래에 Getter/Setter 생성 필요
public class ReviewPhotoVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer photoId;      // photo_id
    private Integer reviewId;     // review_id (FK)
    private String filePath;      // file_path (파일 경로)
    private String originalName;  // original_name (원본 파일명)
    private Integer imgOrder;     // img_order (순서)

    /* Getter / Setter 생략 (Lombok 미사용 시 직접 생성해주세요) */
    public Integer getPhotoId() { return photoId; }
    public void setPhotoId(Integer photoId) { this.photoId = photoId; }
    
    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    
    public Integer getImgOrder() { return imgOrder; }
    public void setImgOrder(Integer imgOrder) { this.imgOrder = imgOrder; }
}