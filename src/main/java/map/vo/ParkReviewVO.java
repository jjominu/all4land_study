package map.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ParkReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer reviewId;       
    private Integer parkId;         
    private Integer memId;          
    private String reviewContent;   
    private Integer rating;         
    private Integer likeCount;      
    private Date createdAt;        
    private Date updatedAt;         

    private String memName;         
    private String memImg;          

   
    private List<ReviewPhotoVO> photoList;



    public Integer getReviewId() {
		return reviewId;
	}
	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}
	public Integer getParkId() {
		return parkId;
	}
	public void setParkId(Integer parkId) {
		this.parkId = parkId;
	}
	public Integer getMemId() {
		return memId;
	}
	public void setMemId(Integer memId) {
		this.memId = memId;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemImg() {
		return memImg;
	}
	public void setMemImg(String memImg) {
		this.memImg = memImg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<ReviewPhotoVO> getPhotoList() { return photoList; }
    public void setPhotoList(List<ReviewPhotoVO> photoList) { this.photoList = photoList; }
}