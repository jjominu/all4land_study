package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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
	public static final String  UPLOAD_PATH ="D:\\upload\\";
	
	  public static FileVO uploadFile(MultipartFile file,int insertId) throws Exception{
	        
		  	FileVO fileVO= new FileVO();	        
	        UUID uid = UUID.randomUUID(); //랜덤으로 붙힐 숫자 생성
	        fileVO.setOriginFileName(file.getOriginalFilename());
	        fileVO.setSaveFileName(uid.toString());
	        fileVO.setBoardId(insertId);
	        fileVO.setFileSize((int)file.getSize());
	        System.out.println((int)file.getSize());
	        fileVO.setFileType(file.getContentType());
	        
	        File target = new File(UPLOAD_PATH,fileVO.getSaveFileName()+fileVO.getOriginFileName());
	        FileCopyUtils.copy(file.getBytes(), target);
	        return fileVO; 
	        
	    }
		public static String imgutil(FileVO fileVO) throws   Exception{
			try {

	        File file= new File(UPLOAD_PATH+fileVO.getSaveFileName()+fileVO.getOriginFileName());

			byte[] imageBytes =FileCopyUtils.copyToByteArray(file);

			String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
			String imageUrl;
			if(fileVO.getFileType().equals("image/png")){
				 imageUrl = "data:"+fileVO.getFileType()+";base64," + imageBase64;
				System.out.println(imageUrl);
			}
			else {
				 imageUrl = null;
			}
			
			return imageUrl;

			}
			catch (Exception e1){

				String imageUrl = null;
				System.out.println(e1);

				return imageUrl;

			}


		}

	    public static void fileDownload(HttpServletResponse response, FileVO fvo) throws Exception {
	        File file = new File(UPLOAD_PATH, fvo.getSaveFileName() + fvo.getOriginFileName());

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
		
	    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
	        "image/jpeg", "image/jpg", "image/png", "image/gif",
	        "application/pdf",
	        "application/haansoft-hwp",                
	        "application/zip",                         
	        "text/plain"
	    );
	    
	    private static final long MAX_SIZE = 1L * 1024 * 1024; 
	    private static final int  MAX_FILES = 5;

	    public static List<String> validateFiles(MultipartFile[] files) {
	        List<String> errors = new ArrayList<>();
	        if (files == null || files.length == 0) return errors;

	        long nonEmpty = Arrays.stream(files).filter(f -> f != null && !f.isEmpty()).count();
	        if (nonEmpty > MAX_FILES) {
	            errors.add("파일은 최대 " + MAX_FILES + "개까지 업로드할 수 있습니다.");
	            return errors;
	        }

	        for (MultipartFile f : files) {
	            if (f == null || f.isEmpty()) continue;

	            String name = f.getOriginalFilename() == null ? "" : f.getOriginalFilename();

	            if (f.getSize() > MAX_SIZE) {
	                errors.add(name + ": 파일 용량은 최대 " + (MAX_SIZE / (1024*1024)) + "MB 입니다.");
	                continue;
	            }

	         
	            String ct = f.getContentType();
	            if (ct == null || ct.isBlank()) ct = "text/plain";
	            if (!ALLOWED_CONTENT_TYPES.contains(ct)) {
	                errors.add(name + ": 허용되지 않은 MIME 타입(" + ct + ")입니다.");
	            }
	        }
	        return errors;
	    }
}
