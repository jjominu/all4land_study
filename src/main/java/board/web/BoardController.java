package board.web;


import java.util.ArrayList;
import java.util.List;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.ModelAndView;


import board.BoardService;
import board.vo.BoardSearchRequestVO;
import board.vo.BoardVO;
import board.vo.CriteriaVO;
import board.vo.FileVO;
import common.PageUtil;

@RestController
@RequestMapping("/board")
@Validated
public class BoardController {
	
	@Resource
	BoardService bs;
	

	@RequestMapping(value="/list.do",method=RequestMethod.GET)//리스트 조회
	public ModelAndView list(@RequestParam( defaultValue = "1") int page ) throws Exception{

		CriteriaVO cri = new CriteriaVO();
		cri.setPage(page);
		
	    PageUtil pageUtil = new PageUtil();
	  
	    pageUtil.setCri(cri);
	    pageUtil.setTotalCount(bs.totalBoardCnt());
	    

		List<BoardVO> list = bs.getList(cri);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/board/list");
		mav.addObject("list",list);
		mav.addObject("pageUtil",pageUtil);

		System.out.println(pageUtil.toString());
		return mav;
	}
	
	@RequestMapping(value="/callBoardWrite.do",method=RequestMethod.GET)//작성페이지접근
	public ModelAndView boardWrite()throws Exception {
		ModelAndView mav = new ModelAndView("/board/boardWrite");
		return mav;
	}
	
	
	
	@RequestMapping(value ="/getDetail.do",method=RequestMethod.POST)//상세정봊조ㅗㅎ
	public ModelAndView getDetail(@RequestParam("boardId") int boardId) throws Exception {
		BoardVO detail = bs.getDetail(boardId);
		BoardVO nextBoard = bs.nextBoard(boardId);
		BoardVO previousBoard= bs.previousBoard(boardId);
		
		List<String> result = new ArrayList<String>();

		
		List<FileVO> fileVO = bs.getFile(detail.getBoardId());
		for(FileVO vo:fileVO ) {
			String resultFileVO = common.FileUtils.imgutil(vo);	
			result.add(resultFileVO);
			
			}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/detail");
		mav.addObject("detail", detail);
		mav.addObject("result",result);
		mav.addObject("nextBoard",nextBoard);
		mav.addObject("previousBoard",previousBoard);

		
		return mav;
	}

	
	@RequestMapping(value="/delete.do" ,method=RequestMethod.POST)
	public ModelAndView deleteBoard(@RequestParam int boardId)throws Exception {
		bs.deleteBoard(boardId);
		return new ModelAndView("redirect:/board/list.do");
	}
	
	@RequestMapping(value="/callBoardUpdate.do", method=RequestMethod.POST)
	public ModelAndView callboardUpdate(@RequestParam("boardId") String boardId)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(boardId) );
		ModelAndView mav = new ModelAndView("/board/boardUpdate");		
		List<FileVO> fileVO = bs.getFile(detail.getBoardId());
		
		mav.addObject("detail",detail);
		mav.addObject("fileVO",fileVO);

		return mav;
	}
	
	@RequestMapping(value ="/updateBoard.do" , method=RequestMethod.POST)
	public ModelAndView boardUpdate(MultipartFile[] file,@RequestParam int boardId,
			@NotNull @RequestParam String title,
														 @RequestParam String content)throws Exception {
		try {
		
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardId(boardId);
		boardVO.setTitle(title);
		boardVO.setContent(content);
		bs.updateBoard(boardVO);
		int insertId = boardVO.getBoardId();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+insertId);
		if(file!=null) {
			for(MultipartFile vo:file )  {
	            String savedName =vo.getOriginalFilename();
	    		if(!savedName.isEmpty()) {
	    			    		
	    			bs.uploadFile(common.FileUtils.uploadFile(vo,insertId));
	    			
	    		}
			}
		}
	}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/board/list.do");
	}
	

  
	@RequestMapping(value="/upload.do", method=RequestMethod.POST)
    public ModelAndView upload(MultipartFile[] file,@NotBlank String title,@RequestParam String content) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(title);
		boardVO.setContent(content);
		int insertId = bs.insertBoard(boardVO);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+insertId);
		for(MultipartFile vo:file )  {
            System.out.println  ("================== file start ==================");
            System.out.println("파일 이름: "+vo.getName());
            System.out.println("파일 실제 이름: "+vo.getOriginalFilename());
            System.out.println("파일 크기: "+vo.getSize());
            System.out.println("content type: "+vo.getContentType());
            System.out.println("================== file   END ==================");
            String savedName =vo.getOriginalFilename();
    		if(!savedName.isEmpty()) {
    			    		
    			bs.uploadFile(common.FileUtils.uploadFile(vo,insertId));
    			
    		}
    		
        }
		
        return new ModelAndView("redirect:/board/list.do");
        
    }
	
	@RequestMapping(value="/search.do", method=RequestMethod.GET)
	public ModelAndView search(@RequestParam String searchType,
								@RequestParam String keyword,
								@RequestParam( defaultValue = "1") int page) {
		BoardSearchRequestVO bsrVO = new BoardSearchRequestVO();
		
		bsrVO.setSearchType(searchType);
		bsrVO.setKeyword(keyword);
		bsrVO.setPage(page);
		
		
		PageUtil pageUtil = new PageUtil(); 
	    pageUtil.setCri(bsrVO);
	    pageUtil.setTotalCount(bs.totalBoardCntbySearch(bsrVO));

	    System.out.println(bsrVO.toString());
		
		List<BoardVO> list = bs.searchBoard(bsrVO);
		ModelAndView mav = new ModelAndView("/board/list");
		mav.addObject("list",list);
		mav.addObject("pageUtil",pageUtil);
		mav.addObject("bsrVO",bsrVO);

		return mav;
	}
	
	@RequestMapping(value="/deleteFile.do",method=RequestMethod.POST)
	public void deleteFile(@RequestParam int fileId) {
		bs.deleteFile(fileId);
	}
	

}
