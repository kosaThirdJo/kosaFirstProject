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

	@Autowired
	private MeetingMapper meetingDao;
	@Autowired
	private UserMapper userDao;

	// 메인페이지 이동
	@GetMapping("/")
	public String goHome(HttpSession session, Model model) {

		// 최신글 조회 (ORDER BY) (현재는 4개만 출력)
		List<MeetingVO> latestMeetings = meetingDao.getLatestMeetings();
		model.addAttribute("latestMeetings", latestMeetings);

		// 인기글 조회 (RANK)
		List<MeetingVO> popularMeetings = meetingDao.getPopularMeetings();
		model.addAttribute("popularMeetings", popularMeetings);

		return "home";
	}

	// 검색페이지로 이동 (searchWord 검색어와 함께)
	@GetMapping("/search")
	public String goSearchPage(@RequestParam("searchWord") String searchWord, Model model) {
		model.addAttribute("searchWord", searchWord);

		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchWord(searchWord);

		// 모임 검색 결과
		List<MeetingVO> searchMeetingResults = meetingDao.searchMeetings(searchdto);
		if(searchMeetingResults.size() != 0){
			// 모임 검색 결과가 있을 경우
			model.addAttribute("searchMeetingResults", searchMeetingResults);
		}else{
			// 모임 검색 결과 없을 경우
			model.addAttribute("noResult", searchMeetingResults);
		}

		// 회원 검색 결과
		List<UserVO> searchUserResults = userDao.searchUser(searchdto);
		if(searchUserResults.size() != 0){
			// 회원 검색 결과 있을 경우
			model.addAttribute("searchUserResults", searchUserResults);
			// 프로필 이미지까지 List형식으로 보내줌
			List<String> encodeImgList = new ArrayList<>();
			for (UserVO user : searchUserResults) {
				if (user.getProfile_photo() != null) {
					String encodeImg = Base64.getEncoder().encodeToString(user.getProfile_photo());
					encodeImgList.add(encodeImg);
				} else {
					encodeImgList.add(null);
				}
			}
			// 회원 검색 결과 - 이미지 List
			model.addAttribute("encodeImgList", encodeImgList);
		}else{
			// 회원 검색 결과 없을 경우
			model.addAttribute("noUser", searchUserResults);
		}
		return "search";
	}

	// 검색페이지에서 모임 글 필터링한 결과
	// 검색어, 카테고리, 관심언어, 관심프레임워크, 관심직무를 가져옴
	@GetMapping(value = "/searchResults", produces = "application/json; charset=utf-8")
	public @ResponseBody List<MeetingVO> getSearchResults(
		@RequestParam("searchWord") String searchWord,
		@RequestParam(name = "category", required = false) List<Integer> category,
		@RequestParam(name = "languages", required = false) List<String> languages,
		@RequestParam(name = "frameworks", required = false) List<String> frameworks,
		@RequestParam(name = "jobs", required = false) List<String> jobs,
		Model model) {

		// SearchDTO
		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchWord(searchWord);
		searchdto.setSearchCategory(category);
		searchdto.setSearchLanguages(languages);
		searchdto.setSearchFrameworks(frameworks);
		searchdto.setSearchJobs(jobs);

		List<MeetingVO> searchMeetingResults;

		// 검색 필터링 선택했을 경우 (null이 아닐경우)
		if ((category != null && !category.isEmpty()) ||
			(languages != null && !languages.isEmpty()) ||
			(frameworks != null && !frameworks.isEmpty()) ||
			(jobs != null && !jobs.isEmpty())) {
			searchMeetingResults = meetingDao.selectMeetingsByFilter(searchdto);
		} else {
			// 검색 필터링 선택하지 않았을 경우
			searchMeetingResults = meetingDao.searchMeetings(searchdto);
		}
		if(searchMeetingResults.size() != 0){
			// 검색 필터링 결과가 있을 경우
			model.addAttribute("searchMeetingResults", searchMeetingResults);
		}else{
			// 검색 필터링 결과가 없을 경우
			model.addAttribute("noResult", searchMeetingResults);
		}

		return searchMeetingResults;
	}


}
