package threestar.selectstar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.MeetingVO;

@Controller
public class HomeController {

	@Autowired
	private MeetingMapper meetingDao;
	private UserMapper userDao;

	@GetMapping("/")
	public String home(HttpSession session, Model model) {
		if(session.getAttribute("user_id") != null){
			int userId = (int) session.getAttribute("user_id");
		}
		// 최신글 조회 (ORDER BY) (현재는 4개만 출력)
		List<MeetingVO> latestMeetings = meetingDao.getLatestMeetings();
		model.addAttribute("latestMeetings", latestMeetings);

		// 인기글 조회 (RANK)
		List<MeetingVO> popularMeetings = meetingDao.getPopularMeetings();
		model.addAttribute("popularMeetings", popularMeetings);

		return "home";
	}

	@GetMapping("/search")
	public String search(@RequestParam("searchWord") String searchWord, Model model) {
		// 검색어
		model.addAttribute("searchWord", searchWord);

		// 모임 - 제목 일치
		List<MeetingVO> searchMeetingResults = meetingDao.searchMeetings(searchWord);
		model.addAttribute("searchMeetingResults", searchMeetingResults);

		return "search";
	}


}
