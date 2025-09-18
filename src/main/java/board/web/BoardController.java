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
	
	 @PostMapping("/items/new")
	    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
	        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
	        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

	        //데이터베이스에 저장
	        Item item = new Item();
	        item.setItemName(form.getItemName());
	        item.setAttachFile(attachFile);
	        item.setImageFiles(storeImageFiles);
	        itemRepository.save(item);

	        redirectAttributes.addAttribute("itemId", item.getId());
	        return "redirect:/items/{itemId}";
	    }
	 @RequestMapping("/insertBoard.do")
		//Command 객체 : 사용자가 전송한 데이터를 매핑한 VO를 바로 생성
		//				사용자 입력 값이 많아지면 코드가 길어지기 때문에 간략화 가능
		//              사용자 입력 input의 name 속성과 VO 멤버변수의 이름을 매핑해주는 것이 중요
		public String insertBoard(BoardVO vo, HttpServletRequest request,
				MultipartHttpServletRequest mhsr) throws IOException {
			System.out.println("글 등록 처리");
			
			//파일 업로드 처리
			MultipartFile uploadFile = vo.getUploadFile();
			if(!uploadFile.isEmpty()) {
				String fileName = uploadFile.getOriginalFilename();
				uploadFile.transferTo(new File("C:/Dev211/" + fileName));
			}
			
			int seq = BoardService.getBoardSeq();
			
			FileUtils fileUtils = new FileUtils();
			List<BoardFileVO> fileList = fileUtils.parseFileInfo(seq, request, mhsr);
			
			boardService.insertBoard(vo);
			
			//화면 네비게이션(게시글 등록 완료 후 게시글 목록으로 이동)
			return "redirect:getBoardList.do";
		}
	
	
}
