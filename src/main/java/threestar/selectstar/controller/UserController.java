package threestar.selectstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.UserDTO;

@Controller
public class UserController {

	@Autowired
	private UserMapper userDAO;

	@Value("${REST_API_KEY}")
	private String apiKey;

	// 회원 가입 페이지 이동
	@GetMapping("/signup")
	public String goSignup(Model model){
		model.addAttribute("apiKey", apiKey);
		return "user/signup";
	}

	// 회원 가입 진행
	@PostMapping("/signup")
	public ModelAndView progressSignup(@ModelAttribute UserDTO userDTO, HttpServletRequest req){
		boolean result = userDAO.signupUser(userDTO);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/");
		return mav;
	}

	/*// 회원 가입 - 중복 확인 (보류)
	@GetMapping("/checkDuplicate")
	public Boolean checkDuplicate(@RequestParam String type, @RequestParam String value){

		Boolean result = false;
		System.out.println(type);
		System.out.println(value);
		result = userDAO.checkDuplicate(type, value);
		System.out.println(result);

		return result;
	}*/

	// 로그인 페이지 이동
	@GetMapping("/login")
	public String login() {
		return "user/login";
	}

	// 로그인 진행
	@PostMapping("/logincheck")
	public String progressLogin(@RequestParam("loginName") String name, @RequestParam("loginPassword") String password, HttpSession session) {
		Integer userId = userDAO.loginUser(name, password);
		if (userId != null && userId > 0) {  // 로그인 성공
			session.setAttribute("user_id", userId);
			session.setMaxInactiveInterval(60 * 30);  // 세션 30분 동안 유지 ( -1 : 무한대 )
			return "redirect:/";
		} else {  // 로그인 실패
			return "user/login";
		}
	}

	// 로그아웃 진행
	@GetMapping("/logout")
	public String progressLogout(HttpSession session) {
		session.invalidate();  // 세션 무효화
		return "redirect:/";
	}

}
