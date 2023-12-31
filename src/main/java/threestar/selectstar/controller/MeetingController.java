package threestar.selectstar.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import threestar.selectstar.dao.ApplyMapper;
import threestar.selectstar.dao.CommentMapper;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.*;
import threestar.selectstar.etc.InterestMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



// 미팅 페이지 메인 컨트롤러
@Controller
@RequestMapping(value = {"/meeting", "meeting?category={category}"})
public class MeetingController {
    // 매퍼 생성자 방식 등록
    final MeetingMapper meetingDao;
    final CommentMapper commentDao;
    final UserMapper userDao;
    final ApplyMapper applyDao;
    public MeetingController(MeetingMapper meetingDao,CommentMapper commentDao, UserMapper userDao, ApplyMapper applyDao) {
        this.meetingDao = meetingDao;
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.applyDao = applyDao;
    }

    final InterestMap interestMap = new InterestMap();
    // 메인페이지
    @GetMapping("")
    public String meetingMain(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                              Model model,
                              @RequestParam(value = "category",required = false) Integer category,
                              @RequestParam(value = "order",required = false,defaultValue = "creation_date")String order){
        List<MeetingVO> allMeetingList = null;
        int allMeetingListCount = 0;
        if (order.equals("meeting_id")){
            order = "creation_date";
        }
        if(category == null){
            allMeetingList = meetingDao.getAllMeetingList(page*12,order);
            allMeetingListCount = meetingDao.getAllMeetingCountList();
        }else{
            allMeetingList = meetingDao.getAllMeetingListByCategory(page*12, category,order);
            allMeetingListCount = meetingDao.getAllMeetingCountListByCategory(category);
        }

        // 관심 분야 한곳에 넣기
        List<List<String>> interestListLang = new ArrayList<>();
        List<List<String>> interestListFrame = new ArrayList<>();
        List<List<String>> interestListJob = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        for (MeetingVO meetingDaoOne:
             allMeetingList) {
            List<String> interestListLangEle = new ArrayList<>();
            List<String> interestListFrameEle = new ArrayList<>();
            List<String> interestListJobEle = new ArrayList<>();
            // 선호 값 가져오기
            String interestLanguage = meetingDaoOne.getInterestLanguage();
            String interestFramework = meetingDaoOne.getInterestFramework();
            String interestJob = meetingDaoOne.getInterestJob();
            // 배열로 만들기 0: 언어, 1: 프레임워크, 2: 직무
            // 배열에 삽입 빈값이면 넣지 말기!
            if (interestLanguage != null){
                String[] SplitInterestLanguage = interestLanguage.split("_");
                for (String splitEle:
                        SplitInterestLanguage) {
                    if (splitEle != null && !splitEle.isEmpty()) {
                        if (interestMap.langMap.containsKey(splitEle)) {
                            interestListLangEle.add(interestMap.langMap.get(splitEle));
                        }

                    }
                }
            }
            if (interestFramework != null){
                String[] SplitInterestFramework = interestFramework.split("_");
                for (String splitEle:
                        SplitInterestFramework) {
                    if (splitEle != null && !splitEle.isEmpty()) {
                        if (interestMap.frameworkMap.containsKey(splitEle)) {
                            interestListFrameEle.add(interestMap.frameworkMap.get(splitEle));
                        }
                    }
                }
            }
            if (interestJob != null) {
                String[] SplitInterestJob = interestJob.split("_");
                for (String splitEle:
                        SplitInterestJob) {
                    if (splitEle != null && !splitEle.isEmpty()) {
                        if (interestMap.jobMap.containsKey(splitEle)) {
                            interestListJobEle.add(interestMap.jobMap.get(splitEle));
                        }
                    }
                }
            }
            //섞기
            Collections.shuffle(interestListLangEle);
            Collections.shuffle(interestListFrameEle);
            Collections.shuffle(interestListJobEle);
            // 관심 분야를 리스트에 넣기
            interestListLang.add(interestListLangEle.subList(0,Math.min(1,interestListLangEle.size())));
            interestListFrame.add(interestListFrameEle.subList(0,Math.min(1,interestListFrameEle.size())));
            interestListJob.add(interestListJobEle.subList(0,Math.min(1,interestListJobEle.size())));
            // 글 생성 날짜 테이블 넣기
            timeList.add(meetingDaoOne.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        // 모델에 넣기
        model.addAttribute("allMeetingListCount",allMeetingListCount); // 페이징 계산을 위한 전체 길이
        model.addAttribute("allMeetingList",allMeetingList); // 페이징 된 미팅 리스트
        // 관심사
        model.addAttribute("interestListLang",interestListLang);
        model.addAttribute("interestListFrame",interestListFrame);
        model.addAttribute("interestListJob",interestListJob);
        // 타임리프에서 사용할 매퍼 클래스
        model.addAttribute("applyDao",applyDao);
        model.addAttribute("userDao",userDao);
        model.addAttribute("commentDao",commentDao);
        model.addAttribute("page",page);// 현재 페이지
        model.addAttribute("pageCategory",category); // 카테고리 해쉬맵
        model.addAttribute("now",LocalDateTime.now()); // 만료됬는지 체크를 위한 현재 시각)
        model.addAttribute("order", order); // 정렬 기준
        return "meeting/meeting_home";
    }
    @GetMapping("/articles")
    public String meetingArticle(@RequestParam("id")Integer meetingId,
                                 HttpServletRequest request,HttpSession session,
                                 Model model){
        // 세션 유저 아이디 조회
        Integer sessionId = null;
        //셀프 체크
        if (session.getAttribute("user_id") != null) {
            sessionId = (Integer) session.getAttribute("user_id");
            int checkApply = applyDao.countApplyByMeetingIDAndUserId(ApplyVO.builder()
                    .userId(sessionId)
                    .meetingId(meetingId)
                    .build());

            model.addAttribute("checkApply",checkApply);
        }

        if (meetingId != null) {
            MeetingDTO meetingDTO = meetingDao.getMeetingArticleById(meetingId);
            // 댓글 리스트 조회
            List<CommentDTO> commentListByMeetingId = commentDao.getCommentListByMeetingId(meetingId);
            // 조회수 1추가
            meetingDao.updateMeetingCount(meetingDTO.getViews()+1,meetingId);
            // 프로필 사진
            byte[] profile_photo = userDao.getProfilePhotoById(meetingDao.getMeetingArticleById(meetingId).getUserId()).getProfile_photo();

            String encodeImg = null;
            if(profile_photo != null) {
                encodeImg = Base64.getEncoder().encodeToString(profile_photo);
            }


            // 조회를 위한 값 저장
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("meetingDTO", meetingDTO);
            model.addAttribute("description",meetingDTO.getDescription().replace("\r\n","<br>").replace(" ","&nbsp"));
            model.addAttribute("commentListByMeetingId", commentListByMeetingId);
            model.addAttribute("userDao",userDao);
            model.addAttribute("count_comment",commentDao.calcCommentCount(meetingId));
            model.addAttribute("applyDao",applyDao);
            model.addAttribute("false","isFail");
            model.addAttribute("image",encodeImg);
            model.addAttribute("now",LocalDateTime.now());
            model.addAttribute("user_id",sessionId);
            // 만약 신청게시물 작성자면...
            return "meeting/meeting_article";
        }

        return "meeting/meeting_article";
    }
    @PostMapping("/articles/{id}")
    public String addMeetingComment(HttpSession session,@PathVariable("id") int meetingId,
                                    String commentContent, HttpServletRequest request, Model model){
        Integer userId = null;
        if (session.getAttribute("user_id") != null){
            userId = (Integer)session.getAttribute("user_id");
            //빌더 수정 할 것..
            CommentDTO commentDTO = new CommentDTO(0,meetingId,userId,commentContent,LocalDateTime.now());
            commentDao.insertComment(commentDTO);
            model.addAttribute("false","isFail");
        model.addAttribute("true","isFail");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
        }
        return "meeting/meeting_article";
    }

    @GetMapping("/write")
    public String writeArticleForm(HttpSession session,Model model){
        model.addAttribute("user_id",session.getAttribute("user_id"));
        model.addAttribute("location",userDao.getUserInfo((int)session.getAttribute("user_id")).getLocation1());
        model.addAttribute("userDao",userDao);
        return "meeting/meeting_form";
    }
    @PostMapping("/write")
    public String writerArticle(String title,
                                int category,
                                LocalDateTime endDate,
                                String location,
                                int recruitNum,
                                String content,
                                @RequestParam("interest_language")String interestLanguage,
                                @RequestParam("interest_framework")String interestFramework,
                                @RequestParam("interest_job") String interestJob,
                                HttpSession session) {
        //HttpSession
        if (session.getAttribute("user_id") != null)
            {
                //빌더 패턴
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
                        .interestLanguage(interestLanguage)
                        .interestFramework(interestFramework)
                        .interestJob(interestJob)
                        .build();

                meetingDao.insertMeeting(meetingDTO);
            }
        return "redirect:" + "/meeting";
    }
    // 게시글 수정 페이지
    @GetMapping("/fix/{id}")
    public String fixArticleForm(Model model,
                             HttpSession session,
                             @PathVariable("id") int meetingId){
        MeetingDTO meetingVO = meetingDao.getMeetingArticleById(meetingId);
        model.addAttribute("user_id",session.getAttribute("user_id"));
        model.addAttribute("meetingVO",meetingVO);
        model.addAttribute("now",LocalDateTime.now());
        return "meeting/meeting_form_fix";
    }
    // 게시글 수정
    @PostMapping("/fix/{id}")
    public String fixArticle(Model model,
                                HttpSession session,
                                @PathVariable("id") int meetingId,
                                String title,
                                int category,
                                @RequestParam("endDate") LocalDateTime endDate,
                                LocalDateTime creationDate,
                                String location,
                                int recruitNum,
                                String content,
                                @RequestParam("interest_language")String interestLanguage,
                                @RequestParam("interest_framework")String interestFramework,
                                @RequestParam("interest_job") String interestJob){
        model.addAttribute("user_id",session.getAttribute("user_id"));
        //meetingDao.update
        meetingDao.updateMeetingById(MeetingDTO.builder()
                        .creationDate(creationDate)
                        .title(title)
                        .category(category)
                        .description(content)
                        .recruitmentCount(recruitNum)
                        .location(location)
                        .meetingId(meetingId)
                        .applicationDeadline(endDate)
                        .interestLanguage(interestLanguage)
                        .interestFramework(interestFramework)
                        .interestJob(interestJob)
                        .build());
        return "redirect:/meeting";
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
    public String deleteArticle(Model model, HttpSession session,
                                @PathVariable("id") int meetingId){
        // 게시글에서 작성한 아이디가 새션 아이디랑 같을시
        if (session.getAttribute("user_id").equals(meetingDao.getMeetingArticleById(meetingId).getUserId())){
            Integer result = applyDao.countApplyByMeetingId(meetingId);
            if (result != null && result !=0){
                model.addAttribute("msg","신청한 인원이 있습니다.");
                return "redirect:/meeting/articles?id="+meetingId;
            }
            model.addAttribute("user_id",session.getAttribute("user_id"));
            meetingDao.deleteMeeting(meetingId);
        }

        return "redirect:/meeting";
    }
    // 지원 햇을경 우
    @PostMapping("/apply/{id}")
    public String addApply(HttpSession session,String emailAddress,
                           @PathVariable("id") int meetingId ,
                           String reason,String snsAddress) {

        if (session.getAttribute("user_id") != null) {
            // 신청 데이터 추가
            applyDao.insertComment(ApplyVO.builder()
                    .userId((int)session.getAttribute("user_id"))
                    .meetingId(meetingId)
                    .emailAddress(emailAddress)
                    .snsAddress(snsAddress)
                    .reason(reason)
                    .applicationDate(LocalDateTime.now())
                    .build());
            // 미팅 데이터에서 미팅인원 1 올리가
            meetingDao.updateApplicationCount(MeetingDTO.builder()
                    .meetingId(meetingId)
                    .applicationCount(applyDao.countApplyByMeetingId(meetingId))
                    .build());
            // 만약 신청인원 이상일 경우 모집완료 하기.
            MeetingDTO meetingDTO = meetingDao.getMeetingArticleById(meetingId);
            if (meetingDTO.getApplicationCount() >= meetingDTO.getRecruitmentCount()){
                meetingDao.updateStatus(1,meetingId);
            }
        }
                return "redirect:/meeting/articles?id=" + meetingId;
    }
    @ResponseBody
    @RequestMapping(value = "/write/cbb", method =RequestMethod.POST)
    public String checkValidate(@RequestBody MeetingDTO meetingDTO ){
        System.out.println();
        return "test";
    }


}
