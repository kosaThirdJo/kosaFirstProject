package threestar.selectstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.MeetingVO;
import threestar.selectstar.domain.UserDTO;
import threestar.selectstar.domain.UserImgFileDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpHeaders;
import java.util.Base64;
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
    public ModelAndView getUserProfileInfo(HttpSession session, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();

        log.info("session user_id 확인"+session.getAttribute("user_id"));

        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));
        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = Base64.getEncoder().encodeToString(imgByte);
        mav.addObject("encodeImg", encodeImg);

        mav.addObject("userDTO", userDTO);
        mav.setViewName("mypage");
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

        log.info("session user_id 확인"+session.getAttribute("user_id"));

        UserDTO userDTO = userDAO.getUserInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = Base64.getEncoder().encodeToString(imgByte);
        mav.addObject("encodeImg", encodeImg);

        mav.addObject("userDTO", userDTO);
        mav.setViewName("myinfo");

        return mav;
    }

    //개인정보수정
    @PostMapping("/updateinfo")
    public String updateUserInfo(UserDTO userDTO){
        log.info("userId  : "+userDTO.getUserId());
        boolean result = userDAO.updateUserInfo(userDTO);
        log.info("update userinfo result >> "+result);
        return "redirect:/mypage/myinfo";
    }
    //프로필 이미지 수정
    @PostMapping("/uploadprofileimg")
    public String updateUserImgFile(@RequestParam("userId") int userId, UserImgFileDTO fileDTO){

        byte[] content = null;
        log.info("id >>>"+userId);
        try{
            content = fileDTO.getProfile_photo().getBytes();
            boolean result = userDAO.updateUserProfileImg(userId, content);
        } catch (Exception e){
            e.printStackTrace();
            log.info("update user profile img exception "+e);
        }
        return "redirect:/mypage/myinfo";
    }

    //내가 만든 모임(내가 작성한 글)
    @GetMapping(value = {"/mymeetinglist", "/mymeetinglist/{category}"})
    public ModelAndView getMyMeetingList(@PathVariable(value = "category", required = false)Integer category, HttpSession session){
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));
        List<MeetingVO> list = null;
        if(category == null){
            list = meetingDAO.getMyMeetingList((int)session.getAttribute("user_id"));
        }else{
            list = meetingDAO.getMyMeetingListByCategory((int)session.getAttribute("user_id"), category);
        }
        log.info("my meeting list "+list);
        ModelAndView mav = new ModelAndView();
        mav.addObject("userDTO", userDTO);

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = Base64.getEncoder().encodeToString(imgByte);
        mav.addObject("encodeImg", encodeImg);

        if(list.size() != 0){
            mav.addObject("meetingvoList", list);
        }else{
            mav.addObject("msg", "조회된 글이 없습니다.");
        }
        mav.addObject("chkcategory", 99);
        mav.setViewName("mymeetinglistView");
        return mav;
    }
    //내가 참여한 모임(내가 신청한 글)
    @GetMapping("/myapplymeetinglist")
    public ModelAndView getMyApplyMeeting(HttpSession session){
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        ModelAndView mav = new ModelAndView();
        mav.addObject("userDTO", userDTO);
        List<MeetingVO> list = meetingDAO.getMyApplyList((int)session.getAttribute("user_id"));

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = Base64.getEncoder().encodeToString(imgByte);
        mav.addObject("encodeImg", encodeImg);

        if(list.size() != 0){
            mav.addObject("applyingvoList", list);
        }else{
            mav.addObject("msg", "조회된 글이 없습니다.");
        }

        mav.addObject("applyingvoList", list);
        mav.setViewName("applymeetinglistView");
        return mav;
    }
}
