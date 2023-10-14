package threestar.selectstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.UserDTO;

@Controller
public class UserController {

	@Autowired
	private UserMapper userDAO;

	// 회원 가입
	@GetMapping("/signup")
	public String showSignUpPage(){
		return "user/signup";
	}

	@PostMapping("/signup")
	public ModelAndView progressSignup(@ModelAttribute UserDTO userDTO, HttpServletRequest req){
		boolean result = userDAO.signupUser(userDTO);
		ModelAndView mav = new ModelAndView();
		if(result){
			// 회원가입 성공 시
			req.setAttribute("successMsg","회원가입이 성공적으로 완료되었습니다.");
		}
		mav.setViewName("redirect:/");
		return mav;
	}

	// 로그인
	@GetMapping("/login")
	public String login() {
		return "user/login";
	}

	@PostMapping("/login")
	public String processLogin(@RequestParam String name, @RequestParam String password, HttpSession session) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(name);
		userDTO.setPassword(password);

		UserDTO loggedInUser = userDAO.loginUser(userDTO);

		if (loggedInUser != null) {
			session.setAttribute("username", loggedInUser.getName());
			return "redirect:/"; // 로그인 성공 시, 메인 페이지로 이동
		} else {
			return "user/login"; // 로그인 실패 시, 로그인 페이지로 다시 이동
		}
	}
}
