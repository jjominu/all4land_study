package board.web;


import java.util.ArrayList;
import java.util.List;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
public class BoardController {
	
	@Resource
	BoardService bs;
	

	@RequestMapping(value="/list.do",method=RequestMethod.GET)//리스트 조회
	public ModelAndView list(HttpServletRequest request) throws Exception{

		CriteriaVO cri = new CriteriaVO();
		if(request.getParameter("page")!=null)cri.setPage(Integer.parseInt(request.getParameter("page")));
		
	    PageUtil pageUtil = new PageUtil();
	  
	    pageUtil.setCri(cri);
	    pageUtil.setTotalCount(bs.totalBoardCnt());
	    System.out.println("CRI.page"+cri.getPage());
	    System.out.println("CRI.pagePer"+cri.getPerPageNum());

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
	
	
//	@RequestMapping("/insertBoard.do")
//	public ModelAndView insertBoard(HttpServletRequest request)throws Exception {
//		BoardVO boardVO = new BoardVO();
//		boardVO.setTitle(request.getParameter("title"));
//		boardVO.setContent(request.getParameter("content"));
//		bs.insertBoard(boardVO);
//		ModelAndView mav = new ModelAndView("redirect:/board/list.do");
//		return mav;
//	}
	
	
	@RequestMapping(value ="/getDetail.do",method=RequestMethod.GET)//상세정봊조ㅗㅎ
	public ModelAndView getDetail(HttpServletRequest request)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(request.getParameter("boardId")));
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
		return mav;
	}

	
	@RequestMapping(value="/delete.do" ,method=RequestMethod.POST)
	public ModelAndView deleteBoard(HttpServletRequest request)throws Exception {
		bs.deleteBoard(Integer.parseInt(request.getParameter("boardId"))  );
		return new ModelAndView("redirect:/board/list.do");
	}
	
	@RequestMapping(value="/callBoardUpdate.do", method=RequestMethod.POST)
	public ModelAndView callboardUpdate(@RequestParam("boardId") String boardId)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(boardId) );
		ModelAndView mav = new ModelAndView("/board/boardUpdate");
		List<String> result = new ArrayList<String>();

		
		List<FileVO> fileVO = bs.getFile(detail.getBoardId());
		
		mav.addObject("detail",detail);
		mav.addObject("fileVO",fileVO);

		return mav;
	}
	
	@RequestMapping(value ="/updateBoard.do" , method=RequestMethod.POST)
	public ModelAndView boardUpdate(MultipartFile[] file,HttpServletRequest request)throws Exception {
		
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardId(Integer.parseInt(request.getParameter("boardId")));
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setContent(request.getParameter("content"));
		bs.updateBoard(boardVO);
		int insertId = boardVO.getBoardId();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+insertId);
		if(file!=null) {
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
		}}
		return new ModelAndView("redirect:/board/list.do");
		}
	
	//	@RequestMapping("/input.do")
//	public String input() {
//		return "upload/input";
//    }
  
	@RequestMapping(value="/upload.do", method=RequestMethod.POST)
    public ModelAndView upload(MultipartFile[] file,HttpServletRequest request) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setContent(request.getParameter("content"));
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
	public ModelAndView search(HttpServletRequest request) {
		BoardSearchRequestVO bsrVO = new BoardSearchRequestVO();
		
	    	
		bsrVO.setSearchType(request.getParameter("searchType"));
		bsrVO.setKeyword(request.getParameter("keyword"));
		if(request.getParameter("page")!=null)bsrVO.setPage(Integer.parseInt(request.getParameter("page")));
		
		
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
	public void deleteFile(HttpServletRequest req) {
		int id = Integer.parseInt(req.getParameter("fileId"));
		bs.deleteFile(id);
	}
	
	
	


   
  
	
	
}
