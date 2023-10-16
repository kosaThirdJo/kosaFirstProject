package threestar.selectstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import threestar.selectstar.domain.MeetingVO;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(HttpSession session, Model model) {
		if(session.getAttribute("user_id") != null){
			int userId = (int) session.getAttribute("user_id");
		}
		return "home";
	}

	@GetMapping("/search")
	public String search(@RequestParam String searchWord, Model model) {
		return "search";
	}
}
