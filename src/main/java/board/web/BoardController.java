package board.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import board.BoardService;
import board.BoardVO;
import board.SearchBoardResponseVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Resource
	BoardService bs;
	

	@RequestMapping("/list.do")
	public ModelAndView list() throws Exception{
		List<BoardVO> list = bs.getList();
		System.out.println(list.get(0).getCreateTimestamp());

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/board/list");
		mav.addObject("list",list);
		return mav;
	}
	
	@RequestMapping("/callBoardWrite.do")
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
	
	
	@RequestMapping("/getDetail.do")
	public ModelAndView getDetail(HttpServletRequest request)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(request.getParameter("boardId")));
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/detail");
		mav.addObject("detail", detail);
		return mav;
	}
	@RequestMapping("/getDetailwithIMG.do")
	public ModelAndView getDetailwithIMG(HttpServletRequest request)throws Exception {

		BoardVO detail = bs.getDetail(Integer.parseInt(request.getParameter("boardId")));
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/detail");
		mav.addObject("detail", detail);
		return mav;
	}
	
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteBoard(HttpServletRequest request)throws Exception {
		bs.deleteBoard(Integer.parseInt(request.getParameter("boardId"))  );
		return new ModelAndView("redirect:/board/list.do");
	}
	
	@RequestMapping("/callBoardUpdate.do")
	public ModelAndView callboardUpdate(HttpServletRequest request)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(request.getParameter("boardId")) );
		ModelAndView mav = new ModelAndView("/board/boardUpdate");
		mav.addObject("detail",detail);
		return mav;
	}
	
	@RequestMapping(value ="/updateBoard.do")
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
  
	@RequestMapping("/upload.do")
    public ModelAndView upload(MultipartFile file,HttpServletRequest request) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setContent(request.getParameter("content"));
		bs.insertBoard(boardVO);
		String savedName = file.getOriginalFilename();
        savedName = common.FileUtils.uploadFile(savedName,file.getBytes());

        return new ModelAndView("redirect:/board/list.do");
        
    }
	
	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request) {
		SearchBoardResponseVO s = new SearchBoardResponseVO();
		s.setSearchType(request.getParameter("searchType"));
		s.setSearchType(request.getParameter("keyword"));
		bs.

	}
    
  
	
	
}
