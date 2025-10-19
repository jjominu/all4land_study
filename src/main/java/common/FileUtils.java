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
		// ====================== 여기서부터 다운로드 관련 유틸 ======================

	    // [추가] 디스크상의 실제 파일 객체 반환 (saveName + originName 조합)
	    public static File resolveDownloadFile(FileVO fvo) {
	        String filename = fvo.getSaveFileName() + fvo.getOriginFileName();
	        return new File(upload_path, filename);
	    }

	    // [추가] Content-Disposition 헤더용 파일명 인코딩 (UTF-8, 공백 처리)
	    public static String contentDispositionFilename(String originFileName) throws Exception {
	        String enc = URLEncoder.encode(originFileName, "UTF-8").replaceAll("\\+", "%20");
	        return "attachment; filename*=UTF-8''" + enc;
	    }

	    // [추가] 파일을 Response로 스트리밍 하는 공통 유틸
	    //  - controller에서 FileVO만 넘기면 됨
	    public static void streamDownload(javax.servlet.http.HttpServletResponse response, FileVO fvo) throws Exception {
	        File file = resolveDownloadFile(fvo);

	        if (!file.exists() || !file.isFile()) {
	            response.setStatus(javax.servlet.http.HttpServletResponse.SC_NOT_FOUND);
	            return;
	        }

	        String ct = (fvo.getFileType()==null || fvo.getFileType().isEmpty())
	                ? "application/octet-stream" : fvo.getFileType();
	        response.setContentType(ct);
	        response.setHeader("Content-Disposition", contentDispositionFilename(fvo.getOriginFileName()));
	        response.setHeader("Content-Length", String.valueOf(file.length()));

	        try (FileInputStream in = new FileInputStream(file)) {
	            FileCopyUtils.copy(in, response.getOutputStream());
	            response.flushBuffer();
	        }
	    }
		
}
