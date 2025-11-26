package map.vo;

import java.io.Serializable;

public class ReviewPhotoVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer photoId;      
    private Integer reviewId;     
    private String filePath;     
    private String originalName;  
    private Integer imgOrder;
	public Integer getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}
	public Integer getReviewId() {
		return reviewId;
	}
	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public Integer getImgOrder() {
		return imgOrder;
	}
	public void setImgOrder(Integer imgOrder) {
		this.imgOrder = imgOrder;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}     

   
}