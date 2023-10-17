package threestar.selectstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import threestar.selectstar.dao.CommentMapper;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.CommentDTO;
import threestar.selectstar.domain.MeetingDTO;
import threestar.selectstar.domain.MeetingVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
    final
    MeetingMapper meetingDao;
    final
    CommentMapper commentDao;
    final
    UserMapper userDao;

    public MeetingController(MeetingMapper meetingDao,CommentMapper commentDao, UserMapper userDao) {
        this.meetingDao = meetingDao;
        this.commentDao = commentDao;
        this.userDao = userDao;
    }

    // 메인페이지
    @GetMapping("")
    public String meetingMain(HttpSession session,Model model){
        // 세션으로
//        Integer userId = null;
//        if(session.getAttribute("user_id") != null){
//            userId = (int) session.getAttribute("user_id");
//        }
        List<MeetingVO> allMeetingList = meetingDao.getAllMeetingList();
        // 관심 분야 한곳에 넣기
        List<List<String>> interestListLang = new ArrayList<>();
        List<List<String>> interestListFrame = new ArrayList<>();
        List<List<String>> interestListJob = new ArrayList<>();

        for (MeetingVO meetingDaoOne:
             allMeetingList) {
            List<String> interestListLangEle = new ArrayList<>();
            List<String> interestListFrameEle = new ArrayList<>();
            List<String> interestListJobEle = new ArrayList<>();
            // 선호 값 가져오기
            String interestLanguage = meetingDaoOne.getInterestLanguage();
            String interestFramework = meetingDaoOne.getInterestFramework();
            String interestJob = meetingDaoOne.getInterestJob();
            // 배열로 만들기 0: 언어, 1: 프레임워크, 2:
            // 배열에 삽입 빈값이면 넣지 말기!
            if (interestLanguage != null){
                String[] SplitInterestLanguage = interestLanguage.split("_");
                for (String splitEle:
                        SplitInterestLanguage) {
                    if (splitEle != null && !splitEle.equals("")) {
                        interestListLangEle.add(splitEle);
                    }
                }
            }
            if (interestFramework != null){
                String[] SplitInterestFramework = interestFramework.split("_");
                for (String splitEle:
                        SplitInterestFramework) {
                    if (splitEle != null &&!splitEle.equals("")) {
                        interestListFrameEle.add(splitEle);
                    }
                }
            }
            if (interestJob != null) {
                String[] SplitInterestJob = interestJob.split("_");
                for (String splitEle:
                        SplitInterestJob) {
                    if (splitEle != null &&!splitEle.equals("")) {
                        interestListJobEle.add(splitEle);
                    }
                }
            }

            // 관심분야를 다시 리스트에 넣기
            interestListLang.add(interestListLangEle);
            interestListFrame.add(interestListFrameEle);
            interestListJob.add(interestListJobEle);
        }
        System.out.println(allMeetingList);
        // 모델에 넣기
        model.addAttribute("allMeetingList",allMeetingList);
        model.addAttribute("interestListLang",interestListLang);
        model.addAttribute("interestListFrame",interestListFrame);
        model.addAttribute("interestListJob",interestListJob);

        return "meeting/meeting_home";
    }
    @GetMapping("/articles")
    public String meetingArticle(@RequestParam("id")Integer meetingId, HttpServletRequest request, Model model){
        //
        if (meetingId != null) {
            // 미팅 vo 조회
            MeetingVO meetingArticle = meetingDao.getMeetingArticleById(meetingId);
            // 댓글 리스트 조회
            List<CommentDTO> commentListByMeetingId = commentDao.getCommentListByMeetingId(meetingId);
            // 조회수 1추가
            meetingDao.updateMeetingCount(meetingArticle.getViews()+1,meetingId);
            // 조회를 위한 값 저장
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("meetingArticle", meetingArticle);
            model.addAttribute("commentListByMeetingId", commentListByMeetingId);
            model.addAttribute("userDao",userDao);
            model.addAttribute("count_comment",commentDao.calcCommentCount(meetingId));
            return "meeting/meeting_article";
        }
        return "meeting/meeting_article";
    }
    @PostMapping("/articles/{id}")
    public String addMeetingComment(HttpSession session,@PathVariable("id") int meetingId,String commentUserName,String commentContent, HttpServletRequest request){
        Integer userId = null;
        if(session.getAttribute("user_id") != null){
            userId = (int) session.getAttribute("user_id");
            // 유저 이름으로 유저 id 조회
            CommentDTO commentDTO = new CommentDTO(0,meetingId,userId,commentContent,LocalDateTime.now());
            commentDao.insertComment(commentDTO);
        }
        // 리다이렉트 댓글
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @GetMapping("/write")
    public String writeArticleForm(HttpSession session){
        Integer userId = null;
        if(session.getAttribute("user_id") != null){
            return "meeting/meeting_form";
        }
        return "user/login";
    }
    @PostMapping("/write")
    public String writerArticle(String title,
                                int category,
                                LocalDateTime endDate,
                                String location,
                                int recruitNum,
                                String content,
                                HttpSession session) {
        //HttpSession
        Integer userId = null;
        if(session.getAttribute("user_id") != null)
            {

                return "redirect:" + "/meeting";
            }


        return "redirect:" + "/meeting";
    }

}
