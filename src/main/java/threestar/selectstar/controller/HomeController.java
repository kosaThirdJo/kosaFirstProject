package threestar.selectstar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.MeetingVO;
import threestar.selectstar.domain.SearchDTO;
import threestar.selectstar.domain.UserVO;

@Controller
public class HomeController {

	@Autowired
	private MeetingMapper meetingDao;
	@Autowired
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
	public String goSearchPage(@RequestParam("searchWord") String searchWord, Model model) {
		model.addAttribute("searchWord", searchWord);

		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchWord(searchWord);

		// 모임 검색
		List<MeetingVO> searchMeetingResults = meetingDao.searchMeetings(searchdto);
		model.addAttribute("searchMeetingResults", searchMeetingResults);

		// 회원 검색
		List<UserVO> searchUserResults = userDao.searchUser(searchdto);
		model.addAttribute(("searchUserResults"), searchUserResults);
		System.out.println(searchUserResults);

		return "search";
	}

	@GetMapping(value = "/searchResults", produces = "application/json; charset=utf-8")
	public @ResponseBody List<MeetingVO> getSearchResults(
		@RequestParam("searchWord") String searchWord,
		@RequestParam(name = "category", required = false) List<Integer> category,
		@RequestParam(name = "languages", required = false) List<String> languages,
		@RequestParam(name = "frameworks", required = false) List<String> frameworks,
		@RequestParam(name = "jobs", required = false) List<String> jobs,
		Model model) {

		System.out.println(searchWord);
		System.out.println(category);
		System.out.println(languages);
		System.out.println(frameworks);
		System.out.println(jobs);

		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchWord(searchWord);
		searchdto.setSearchCategory(category);
		searchdto.setSearchLanguages(languages);
		searchdto.setSearchFrameworks(frameworks);
		searchdto.setSearchJobs(jobs);

		List<MeetingVO> searchMeetingResults;

		if ((category != null && !category.isEmpty()) ||
			(languages != null && !languages.isEmpty()) ||
			(frameworks != null && !frameworks.isEmpty()) ||
			(jobs != null && !jobs.isEmpty())) {
			System.out.println("filter");
			// 모임 검색 - 필터링
			searchMeetingResults = meetingDao.selectMeetingsByFilter(searchdto);
		} else {
			System.out.println("non");
			// 모임 검색 - 제목만
			searchMeetingResults = meetingDao.searchMeetings(searchdto);
			System.out.println(searchMeetingResults);
		}
		model.addAttribute("searchMeetingResults", searchMeetingResults);

		return searchMeetingResults;
	}


}
