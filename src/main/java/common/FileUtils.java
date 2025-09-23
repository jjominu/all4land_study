package common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.vo.FileVO;

public class FileUtils {
	//@Resource(name = "upload_path") // Servlet-content.xml 의 이름과 맞아야함! bean등록필수
	public static String  upload_path ="C:\\Users\\dadsd\\Desktop\\upload";
	
	  public static String uploadFile(String originalName,byte[] fileData) throws Exception{
	        
	        //uuid 생성
	        
	        UUID uid = UUID.randomUUID(); //랜덤으로 붙힐 숫자 생성
	        String savedName = uid.toString()+"_"+originalName; //저장할이름에 랜덤숫자+파일이름
	        File target = new File(upload_path,savedName);
	        //파일 복사
	        FileCopyUtils.copy(fileData, target);
	        return savedName; 
	        
	    }
		public static String imgutil(String fileName) throws   Exception{
			try {
	        File file= new File(upload_path+fileName);
			byte[] imageBytes =FileCopyUtils.copyToByteArray(file);
			String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
			String imageUrl = "data:image/png;base64," + imageBase64;
			return imageUrl;

			}
			catch (Exception e1){
				String imageUrl = null;
				return imageUrl;

			}


		}
}
