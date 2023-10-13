package threestar.selectstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.UserDTO;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    UserMapper userDAO;
    @RequestMapping("/profile")
    //public ModelAndView getUserProfileInfo(){
    public void getUserProfileInfo(){
        ModelAndView mav = new ModelAndView();
        UserDTO userDTO = userDAO.getUserProfileInfo(1);
        mav.addObject("userDTO", userDTO);
        mav.setViewName("profile");
        System.out.println(userDTO);
        System.out.println(userDTO.getAbout_me());
        System.out.println(userDTO.getProfile_content());
        //return mav;
    }
}
