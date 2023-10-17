package threestar.selectstar.controller;

import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.MeetingVO;
import threestar.selectstar.domain.UserDTO;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private static final Logger log = LoggerFactory.getLogger(MyPageController.class);
    @Autowired
    UserMapper userDAO;
    @Autowired
    MeetingMapper meetingDAO;

    //이력관리 조회
    @GetMapping("/profile")
    public ModelAndView getUserProfileInfo(HttpSession session){
        ModelAndView mav = new ModelAndView();

        log.info("session user_id 확인"+session.getAttribute("user_id"));
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));
        mav.addObject("userDTO", userDTO);
        mav.setViewName("mypage");
        log.info("id 확인 : "+userDTO.getUserId());
        log.info(""+userDTO);
        log.info("프로필이미지"+userDTO.getProfile_photo());
        return mav;
    }

    //이력관리 수정
    @PostMapping("/updateprofile")
    public String updateUserProfileInfo(@ModelAttribute UserDTO userDTO, Model model){
        log.info("userid "+userDTO.getUserId());
        boolean result = userDAO.updateProfileInfo(userDTO);
        log.info("result "+result);
        if(result) {
            model.addAttribute("updateresult", "이력 수정 완료되었습니다.");
        }else{
            model.addAttribute("updateresult", "이력 수정 실패했습니다.");
        }
        return "redirect:/mypage/profile";
    }
    
    //개인정보수정 조회
    @GetMapping("/myinfo")
    public ModelAndView getUserInfo(HttpSession session){
        ModelAndView mav = new ModelAndView();
        //세션 수정 후 반영 예정
        log.info("session user_id 확인"+session.getAttribute("user_id"));
        UserDTO userDTO = userDAO.getUserInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));
        mav.addObject("userDTO", userDTO);
        mav.setViewName("myinfo");
        log.info(""+userDTO);
        return mav;
    }

    //개인정보수정
    @PostMapping("/updateinfo")
    public String updateUserInfo(UserDTO userDTO){
        log.info("userId  : "+userDTO.getUserId());
        log.info("updateuserinfo"+userDTO);
        boolean result = userDAO.updateUserInfo(userDTO);
        log.info("update userinfo result "+result);
        return "redirect:/mypage/myinfo";
    }

    //내가 만든 모임(내가 작성한 글)
    @GetMapping("/mymeetinglist")
    public ModelAndView getMyMeetingList(HttpSession session){
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        log.info("/mymeetinglist user id >> "+(int)session.getAttribute("user_id"));

        List<MeetingVO> list = meetingDAO.getMyMeetingList(1);
        log.info("my meeting list count "+list.size());
        log.info("my meeting list "+list);
        ModelAndView mav = new ModelAndView();
        mav.addObject("userDTO", userDTO);

        if(list.size() != 0){
            mav.addObject("meetingvoList", list);
        }else{
            mav.addObject("msg", "아직 작성한 모집 글이 없습니다.");
        }

        mav.setViewName("mymeetinglistView");
        return mav;
    }

}
