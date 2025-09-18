package board.web;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import board.BoardService;
import board.BoardVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Resource
	BoardService bs;
	@RequestMapping("/list.do")
	public ModelAndView list() throws Exception{
		List<BoardVO> list = bs.getList();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/list");
		mav.addObject("list",list);
		return mav;
	}
	
	@RequestMapping("/callBoardWrite.do")
	public ModelAndView boardWrite()throws Exception {
		
		ModelAndView mav = new ModelAndView("/board/boardWrite");
		return mav;
	}
	@RequestMapping("/insertBoard.do")
	public ModelAndView insertBoard(HttpServletRequest request)throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setId(Integer.parseInt(request.getParameter("id")));
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setContent(request.getParameter("content"));

		bs.insertBoard(boardVO);
		ModelAndView mav = new ModelAndView("redirect:/board/list.do");
		return mav;
	}
	
	@RequestMapping("/getDetail.do")
	public ModelAndView getDetail(HttpServletRequest request)throws Exception {
		BoardVO detail = bs.getDetail(Integer.parseInt(request.getParameter("id")));
		System.out.println(detail);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/detail");
		mav.addObject("detail", detail);
		return mav;
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteBoard(HttpServletRequest request)throws Exception {
		bs.deleteBoard(Integer.parseInt(request.getParameter("id"))  );
		
		return new ModelAndView("redirect:/board/list.do");
	}
	
	
}
