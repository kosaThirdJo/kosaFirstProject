package threestar.selectstar.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import threestar.selectstar.dao.CommentMapper;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.CommentDTO;
import threestar.selectstar.domain.MeetingVO;
import threestar.selectstar.domain.SearchDTO;
import threestar.selectstar.domain.UserVO;

@Controller
public class HomeController {

	private final MeetingMapper meetingDao;
	private final UserMapper userDao;

	public HomeController(MeetingMapper meetingDao, UserMapper userDao) {
		this.meetingDao = meetingDao;
		this.userDao = userDao;
	}

	@GetMapping("/")
	public String home(HttpSession session, Model model) {

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
		if(searchMeetingResults.size() != 0){
			model.addAttribute("searchMeetingResults", searchMeetingResults);
		}else{
			model.addAttribute("noResult", searchMeetingResults);
		}

		// 회원 검색
		List<UserVO> searchUserResults = userDao.searchUser(searchdto);
		if(searchUserResults.size() != 0){
			model.addAttribute("searchUserResults", searchUserResults);

			List<String> encodeImgList = new ArrayList<>();
			for (UserVO user : searchUserResults) {
				if (user.getProfile_photo() != null) {
					String encodeImg = Base64.getEncoder().encodeToString(user.getProfile_photo());
					encodeImgList.add(encodeImg);
				} else {
					encodeImgList.add(null);
				}
			}
			model.addAttribute("encodeImgList", encodeImgList);
		}else{
			model.addAttribute("noUser", searchUserResults);
		}
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

		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchWord(searchWord);
		searchdto.setSearchCategory(category);
		searchdto.setSearchLanguages(languages);
		searchdto.setSearchFrameworks(frameworks);
		searchdto.setSearchJobs(jobs);

		List<MeetingVO> searchMeetingResults;

		// 모임 검색
		if ((category != null && !category.isEmpty()) ||
			(languages != null && !languages.isEmpty()) ||
			(frameworks != null && !frameworks.isEmpty()) ||
			(jobs != null && !jobs.isEmpty())) {
			// 모임 검색 - 필터링
			searchMeetingResults = meetingDao.selectMeetingsByFilter(searchdto);
		} else {
			// 모임 검색 - 제목만
			searchMeetingResults = meetingDao.searchMeetings(searchdto);
		}
		if(searchMeetingResults.size() != 0){
			model.addAttribute("searchMeetingResults", searchMeetingResults);
		}else{
			model.addAttribute("noResult", searchMeetingResults);
		}

		return searchMeetingResults;
	}


}
