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
import java.util.*;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
    final
    MeetingMapper meetingDao;
    final
    CommentMapper commentDao;
    final
    UserMapper userDao;
    final static List<String> categoryInfo =  new ArrayList<>(Arrays.asList("스터디","프로젝트","기타"));
    final static List<String> statusInfo =  new ArrayList<>(Arrays.asList("모집중","모집완료","삭제"));

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
        // 모델에 넣기
        model.addAttribute("allMeetingList",allMeetingList);
        model.addAttribute("interestListLang",interestListLang);
        model.addAttribute("interestListFrame",interestListFrame);
        model.addAttribute("interestListJob",interestListJob);

        model.addAttribute("categoryInfo",categoryInfo);
        model.addAttribute("statusInfo",statusInfo);
        return "meeting/meeting_home";
    }
    @GetMapping("/articles")
    public String meetingArticle(@RequestParam("id")Integer meetingId, HttpServletRequest request,HttpSession session, Model model){
        // 세션 유저 아이디 조회
        Integer sessionId = null;
        if (session.getAttribute("user_id") != null) {
            sessionId = (Integer) session.getAttribute("user_id");
        }
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
            model.addAttribute("false","isFail");
            model.addAttribute("user_id",sessionId);
            // 만약 신청게시물 작성자면...
            return "meeting/meeting_article";
        }

        return "meeting/meeting_article";
    }
    @PostMapping("/articles/{id}")
    public String addMeetingComment(HttpSession session,@PathVariable("id") int meetingId,String commentUserName,String commentContent, HttpServletRequest request, Model model){
        Integer userId = null;
        if (session.getAttribute("user_id") != null){
            userId = (Integer)session.getAttribute("user_id");
            CommentDTO commentDTO = new CommentDTO(0,meetingId,userId,commentContent,LocalDateTime.now());
            commentDao.insertComment(commentDTO);

            model.addAttribute("false","isFail");
        }

        model.addAttribute("true","isFail");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @GetMapping("/write")
    public String writeArticleForm(HttpSession session,Model model){
        model.addAttribute("user_id",session.getAttribute("user_id"));
        return "meeting/meeting_form";
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
        if (session.getAttribute("user_id") != null)
            {
                MeetingDTO meetingDTO = MeetingDTO.builder()
                        .userId((int)session.getAttribute("user_id"))
                        .title(title)
                        .category(category)
                        .applicationDeadline(endDate)
                        .location(location)
                        .recruitmentCount(recruitNum)
                        .description(content)
                        .applicationCount(0)
                        .creationDate(LocalDateTime.now())
                        .build();
            }
        return "redirect:" + "/meeting";
    }
    // 게시글 수정 페이지
    @GetMapping("/fix/{id}")
    public String fixArticle(Model model, HttpSession session, @PathVariable String id){
        model.addAttribute("user_id",session.getAttribute("user_id"));
        return "meeting/meeting_form_fix";
    }
    // 게시글 수정
    @PostMapping("/fix/{id}")
    public String fixArticleadd(Model model, HttpSession session, @PathVariable String id){
        model.addAttribute("user_id",session.getAttribute("user_id"));
        // id 조회후 다르면 강제 이동.
        // 같으면 페이지 값 가져와서 화면에 뿌리기
        return "redirect:" +"meeting";
    }
    // 모집 완료
    @GetMapping("/finish/{id}")
    public String finishArticle(Model model, HttpSession session, @PathVariable("id") int meetingId){
        // 게시글에서 작성한 아이디가 새션 아이디랑 같을시
        if (session.getAttribute("user_id").equals(meetingDao.getMeetingArticleById(meetingId).getUserId())){
            model.addAttribute("user_id",session.getAttribute("user_id"));
            meetingDao.updateStatus(1,meetingId);
        }
        return "redirect:/meeting";
    }
    // 게시글 삭제(비활성화)
    @GetMapping("/remove/{id}")
    public String deleteArticle(Model model, HttpSession session, @PathVariable("id") int meetingId){
        // 게시글에서 작성한 아이디가 새션 아이디랑 같을시
        if (session.getAttribute("user_id").equals(meetingDao.getMeetingArticleById(meetingId).getUserId())){
            model.addAttribute("user_id",session.getAttribute("user_id"));
            meetingDao.deleteMeeting(meetingId);
        }
        // 엘스 비정상정인 접근입니다.
        return "redirect:/meeting";
    }
}
