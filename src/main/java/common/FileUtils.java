package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.vo.FileVO;

public class FileUtils {
	//@Resource(name = "upload_path") // Servlet-content.xml 의 이름과 맞아야함! bean등록필수
	public static String  upload_path ="D:\\upload\\";
	
	  public static FileVO uploadFile(MultipartFile file,int insertId) throws Exception{
	        
		  	FileVO fileVO= new FileVO();	        
	        UUID uid = UUID.randomUUID(); //랜덤으로 붙힐 숫자 생성
	        fileVO.setOriginFileName(file.getOriginalFilename());
	        fileVO.setSaveFileName(uid.toString());
	        fileVO.setBoardId(insertId);
	        fileVO.setFileSize((int)file.getSize());
	        System.out.println((int)file.getSize());
	        fileVO.setFileType(file.getContentType());
	        
	        File target = new File(upload_path,fileVO.getSaveFileName()+fileVO.getOriginFileName());
	        FileCopyUtils.copy(file.getBytes(), target);
	        return fileVO; 
	        
	    }
		public static String imgutil(FileVO fileVO) throws   Exception{
			try {

	        File file= new File(upload_path+fileVO.getSaveFileName()+fileVO.getOriginFileName());

			byte[] imageBytes =FileCopyUtils.copyToByteArray(file);

			String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

			String imageUrl = "data:"+fileVO.getFileType()+";base64," + imageBase64;
			System.out.println(imageUrl);
			return imageUrl;

			}
			catch (Exception e1){

				String imageUrl = null;
				System.out.println(e1);

				return imageUrl;

			}


		}

	    public static void fileDownload(HttpServletResponse response, FileVO fvo) throws Exception {
	        File file = new File(upload_path, fvo.getSaveFileName() + fvo.getOriginFileName());

	        String ct = (fvo.getFileType()==null || fvo.getFileType().isEmpty())
	                ? "application/octet-stream" : fvo.getFileType();
	        response.setContentType(ct);
	        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fvo.getOriginFileName(), "UTF-8").replaceAll("\\+", "%20"));
	        response.setHeader("Content-Length", String.valueOf(file.length()));

	        try (FileInputStream in = new FileInputStream(file)) {
	            FileCopyUtils.copy(in, response.getOutputStream());
	            response.flushBuffer();
	        }
	    }
		
}
