package threestar.selectstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${REST_API_KEY}")
    private String apiKey;

    //다른 이용자 이력 조회
    @GetMapping("/profileinfo")
    public ModelAndView getUserProfile(@RequestParam("id")Integer userId){
        UserDTO userDTO = userDAO.getProfileInfo(userId);
        ModelAndView mav = new ModelAndView();
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
        //log.info("encodeImg >>"+encodeImg);
        mav.addObject("encodeImg", encodeImg);
        mav.addObject("userDTO", userDTO);
        mav.setViewName("userprofile");
        return mav;
    }

    //이력관리 조회
    @GetMapping("/profile")
    public ModelAndView getMyProfileInfo(HttpSession session, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();

        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
        mav.addObject("encodeImg", encodeImg);

        mav.addObject("userDTO", userDTO);
        mav.setViewName("myprofile");
        return mav;
    }

    //이력관리 수정
    @PostMapping("/updateprofile")
    public String updateUserProfileInfo(@ModelAttribute UserDTO userDTO, Model model){
        log.info("userid "+userDTO.getUserId());
        boolean result = userDAO.updateProfileInfo(userDTO);

        log.info("result >> "+result);
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
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
        mav.addObject("encodeImg", encodeImg);

        //지역인증시 필요한 apikey
        mav.addObject("apiKey", apiKey);
        mav.addObject("userDTO", userDTO);
        mav.setViewName("myinfo");

        return mav;
    }

    //개인정보수정
    @PostMapping("/updateinfo")
    public String updateUserInfo(UserDTO userDTO){
        log.info("userDTO  :"+userDTO);
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

    //닉네임 중복 검사
    @GetMapping(value = "/checknickname", produces = "application/json; charset=utf-8")
    public @ResponseBody boolean chkNickname(@RequestParam("nickname") String nickname){
        boolean result = false;
        //chk 가 0이면 사용 가능. 1이상이면 중복
        int chk = userDAO.checkNickname(nickname);
        if(chk == 0){
            result = true;
        }
        return result;
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
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
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
    //내가 작성한 글(수정)
    @GetMapping(value = "/mymeetingbyfilter", produces = "application/json; charset=utf-8")
    public @ResponseBody List<MeetingVO> getMyMeetingListBySearch(
            @RequestParam(name = "category", required = false) String strCategory,
            @RequestParam(name="status", required = false) String strStatus,
            Model model, HttpSession session){
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        System.out.println(strCategory);
        System.out.println(strStatus);

        int category = 0;
        int status = 0;

        if(strCategory != null) {
            switch (strCategory) {
                case "project":
                    category = 1;
                    break;
                case "study":
                    category = 0;
                    break;
                case "etc":
                    category = 2;
                    break;
                case "all":
                    strCategory = null;
                    break;
            }
        }
        if(strStatus != null) {
            switch (strStatus) {
                case "statusing":
                    status = 0;
                    break;
                case "statused":
                    status = 1;
                    break;
                case "all":
                    strStatus = null;
                    break;
            }
        }
        List<MeetingVO> list;
        if((strStatus != null && !strStatus.isEmpty()) && (strCategory != null && !strCategory.isEmpty())){
            // 카테고리(프로젝트/스터디/기타)와 모집여부(모집중/모집완료) 선택 시
            list = meetingDAO.getMyMeetingListByCateStatus((int)session.getAttribute("user_id"), category, status);
            System.out.println(0);
        }else if((strStatus == null) && (strCategory != null && !strCategory.isEmpty())){
            // 카테고리(전체) 와 모집여부(모집중/모집완료) 선택 시
            list = meetingDAO.getMyMeetingListByCategory((int)session.getAttribute("user_id"), category);
            System.out.println(1);
        }else if((strStatus != null && !strStatus.isEmpty()) && (strCategory == null)){
            // 카테고리(프로젝트/스터디/기타)와 모집여부(전체) 선택 시
            list = meetingDAO.getMyMeetingListByStatus((int)session.getAttribute("user_id"), status);
            System.out.println(2);
        }else{
            //카테고리(전체)와 모집여부(전체) 선택 시
            list = meetingDAO.getMyMeetingList((int)session.getAttribute("user_id"));
            System.out.println(list.get(0));
        }

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("encodeImg", encodeImg);
        model.addAttribute("userId", (int)session.getAttribute("user_id"));

        if(list.size() != 0){
            model.addAttribute("meetingvoList", list);
        }else{
            model.addAttribute("msg", "조회된 글이 없습니다.");
        }
        return list;
    }

    //내가 참여한 모임(내가 신청한 글)
    @GetMapping("/myapplymeetinglist")
    public ModelAndView getMyApplyMeeting(HttpSession session){
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        ModelAndView mav = new ModelAndView();
        mav.addObject("userDTO", userDTO);
        List<MeetingVO> applylist = meetingDAO.getMyApplyList((int)session.getAttribute("user_id"));

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
        mav.addObject("encodeImg", encodeImg);

        if(applylist.size() != 0){
            mav.addObject("applyingvoList", applylist);
        }else{
            mav.addObject("msg", "조회된 글이 없습니다.");
        }
        mav.addObject("applyingvoList", applylist);
        mav.setViewName("applymeetinglistView");
        return mav;
    }

    //내가 참여한 모임(내가 신청한 글) 카테고리별 - 모집상태별 조회
    @GetMapping(value = "/myapplyingfilter", produces = "application/json; charset=utf-8")
    public @ResponseBody List<MeetingVO> getMyApplyMeetingBySearch(
            @RequestParam(name = "category", required = false) String strCategory,
            @RequestParam(name="status", required = false) String strStatus,
            Model model, HttpSession session){
        UserDTO userDTO = userDAO.getUserProfileInfo((int)session.getAttribute("user_id"));
        userDTO.setUserId((int)session.getAttribute("user_id"));

        int category = 0;
        int status = 0;

        if(strCategory != null) {
            switch (strCategory) {
                case "project":
                    category = 1;
                    break;
                case "study":
                    category = 0;
                    break;
                case "etc":
                    category = 2;
                    break;
                case "all":
                    strCategory = null;
                    break;
            }
        }
        if(strStatus != null) {
            switch (strStatus) {
                case "statusing":
                    status = 0;
                    break;
                case "statused":
                    status = 1;
                    break;
                case "all":
                    strStatus = null;
                    break;
            }
        }
        List<MeetingVO> list;
        if((strStatus != null && !strStatus.isEmpty()) && (strCategory != null && !strCategory.isEmpty())){
            // 카테고리(프로젝트/스터디/기타)와 모집여부(모집중/모집완료) 선택 시
            list = meetingDAO.getMyApplyListByCateStatus((int)session.getAttribute("user_id"), category, status);
        }else if((strStatus == null) && (strCategory != null && !strCategory.isEmpty())){
            // 카테고리(전체) 와 모집여부(모집중/모집완료) 선택 시
            list = meetingDAO.getMyApplyListByCategory((int)session.getAttribute("user_id"), category);
        }else if((strStatus != null && !strStatus.isEmpty()) && (strCategory == null)){
            // 카테고리(프로젝트/스터디/기타)와 모집여부(전체) 선택 시
            list = meetingDAO.getMyApplyListByStatus((int)session.getAttribute("user_id"), status);
        }else{
            //카테고리(전체)와 모집여부(전체) 선택 시
            list = meetingDAO.getMyApplyList((int)session.getAttribute("user_id"));
        }

        //마이페이지 side bar -프로필 이미지
        byte[] imgByte = userDTO.getProfile_photo();
        String encodeImg = null;
        if(imgByte != null) {
            encodeImg = Base64.getEncoder().encodeToString(imgByte);
        }
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("encodeImg", encodeImg);
        model.addAttribute("userId", (int)session.getAttribute("user_id"));

        if(list.size() != 0){
            model.addAttribute("applyingvoList", list);
        }else{
            model.addAttribute("msg", "조회된 글이 없습니다.");
        }
        return list;
    }
    
}
