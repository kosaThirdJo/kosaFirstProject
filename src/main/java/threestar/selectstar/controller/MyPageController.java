package threestar.selectstar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.UserDTO;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private static final Logger log = LoggerFactory.getLogger(MyPageController.class);
    @Autowired
    UserMapper userDAO;

    //이력관리 조회
    @GetMapping("/profile")
    public ModelAndView getUserProfileInfo(){
        ModelAndView mav = new ModelAndView();
        //session 수정 후 반영 예정
        UserDTO userDTO = userDAO.getUserProfileInfo(1);
        mav.addObject("userDTO", userDTO);
        mav.setViewName("mypage");
        log.info("id 확인 : "+userDTO.getUserId());
        log.info(userDTO.getProfile_content());
        log.info(userDTO.getAbout_me());
        log.info(userDTO.getNickname());
        log.info("프로필이미지"+userDTO.getProfile_photo());
        return mav;
    }

    //이력관리 수정
    @PostMapping("/updateprofile")
    public String updateUserProfileInfo(UserDTO userDTO){
        log.info("userid "+userDTO.getUserId());
        log.info(userDTO.getAbout_me());
        log.info(userDTO.getProfile_content());
        boolean result = userDAO.updateProfileInfo(userDTO);
        log.info("result "+result);
        return "redirect:/mypage/profile";
    }
    
    //개인정보수정 조회
    @GetMapping("/myinfo")
    public ModelAndView getUserInfo(){
        ModelAndView mav = new ModelAndView();
        //세션 수정 후 반영 예정
        UserDTO userDTO = userDAO.getUserInfo(1);
        mav.addObject("userDTO", userDTO);
        mav.setViewName("myinfo");
        log.info(""+userDTO);
        return mav;
    }

    //개인정보수정
    @PostMapping("/updateinfo")
    public String updateUserInfo(UserDTO userDTO){
        log.info("userId  : "+userDTO.getUserId());
        log.info(userDTO.getPassword());
        log.info(userDTO.getEmail());
        log.info(userDTO.getNickname());
        log.info(userDTO.getLocation1());
        log.info("updateuserinfo"+userDTO);
        //관심분야 추후 추가
        boolean result = userDAO.updateUserInfo(userDTO);
        log.info("result "+result);
        return "redirect:/mypage/myinfo";
    }
}
