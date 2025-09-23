package board.web;


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
		detail.setImgName(common.FileUtils.imgutil(detail.getImgName()));
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/detail");
		mav.addObject("detail", detail);
		return mav;
	}

	
	@RequestMapping(value="/delete.do" )
	public ModelAndView deleteBoard(HttpServletRequest request)throws Exception {
		bs.deleteBoard(Integer.parseInt(request.getParameter("boardId"))  );
		return new ModelAndView("redirect:/board/list.do");
	}
	
	@RequestMapping(value="/callBoardUpdate.do", method=RequestMethod.POST)
	public ModelAndView callboardUpdate(@RequestParam("boardId") String boardId)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(boardId) );
		ModelAndView mav = new ModelAndView("/board/boardUpdate");
		mav.addObject("detail",detail);
		return mav;
	}
	
	@RequestMapping(value ="/updateBoard.do" , method=RequestMethod.POST)
	public ModelAndView boardUpdate(HttpServletRequest request)throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardId(Integer.parseInt(request.getParameter("boardId")));
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setContent(request.getParameter("content"));
		bs.updateBoard(boardVO);
		return new ModelAndView("redirect:/board/list.do");
	}
	//	@RequestMapping("/input.do")
//	public String input() {
//		return "upload/input";
//    }
  
	@RequestMapping(value="/upload.do", method=RequestMethod.POST)
    public ModelAndView upload(MultipartFile file,HttpServletRequest request) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setContent(request.getParameter("content"));
		String savedName = file.getOriginalFilename();
		if(!savedName.isEmpty()) {
			savedName = common.FileUtils.uploadFile(savedName,file.getBytes());
		}
        boardVO.setImgName(savedName);
		bs.insertBoard(boardVO);

        return new ModelAndView("redirect:/board/list.do");
        
    }
	
	@RequestMapping(value="/search.do", method=RequestMethod.GET)
	public ModelAndView search(HttpServletRequest request) {
		BoardSearchRequestVO bsrVO = new BoardSearchRequestVO();
		
		bsrVO.setSearchType(request.getParameter("searchType"));
		bsrVO.setKeyword(request.getParameter("keyword"));
		List<BoardVO> list = bs.searchBoard(bsrVO);
		ModelAndView mav = new ModelAndView("/board/list");
		mav.addObject("list",list);

		return mav;
	}
	
	
	


   
  
	
	
}
