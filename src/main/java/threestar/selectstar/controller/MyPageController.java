package threestar.selectstar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/profile")
    public ModelAndView getUserProfileInfo(){
        ModelAndView mav = new ModelAndView();
        UserDTO userDTO = userDAO.getUserProfileInfo(1);
        mav.addObject("userDTO", userDTO);
        mav.setViewName("mypage");
        log.info(userDTO.getProfile_content());
        log.info(userDTO.getAbout_me());
        log.info(userDTO.getNickname());
        log.info("프로필이미지"+userDTO.getProfile_photo());
        return mav;
    }

    @PostMapping("/updateprofile")
    public String updateUserProfileInfo(UserDTO userDTO){
        log.info("userid "+userDTO.getUserId());
        log.info(userDTO.getAbout_me());
        log.info(userDTO.getProfile_content());
        boolean result = userDAO.updateProfileInfo(userDTO);
        log.info("result "+result);
        return "redirect:/mypage/profile";
    }
}
