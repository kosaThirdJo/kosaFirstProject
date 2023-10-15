package threestar.selectstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.domain.MeetingVO;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
    final
    MeetingMapper meetingDao;

    public MeetingController(MeetingMapper meetingDao) {
        this.meetingDao = meetingDao;
    }

    @GetMapping("")
    public String meetingMain(Model model){
        List<MeetingVO> allMeetingList = meetingDao.getAllMeetingList();
        model.addAttribute("allMeetingList",allMeetingList);
        // 관심 분야 한곳에 넣기
        List<List<String>> interestListLang = new ArrayList<>();
        List<List<String>> interestListFrame = new ArrayList<>();
        List<List<String>> interestListJob = new ArrayList<>();
        int idx = 0;
        for (MeetingVO meetingDaoOne:
             allMeetingList) {
            List<String> interestListLangEle = new ArrayList<>();
            List<String> interestListFrameEle = new ArrayList<>();
            List<String> interestListJobEle = new ArrayList<>();
            // 선호 값 가져오기
            String interestLanguage = meetingDaoOne.getInterestLanguage();
            String interestFramework = meetingDaoOne.getInterestFramework();
            String interestJob = meetingDaoOne.getInterestJob();
            System.out.println(interestLanguage);
            // 배열로 만들기 0: 언어, 1: 프레임워크, 2:
            // 배열에 삽입 빈값이면 넣지 말기!
            if (interestLanguage != null && !interestLanguage.isEmpty()){
                String[] SplitInterestLanguage = interestLanguage.split("_");
                for (String splitEle:
                        SplitInterestLanguage) {
                    if (splitEle != null && !splitEle.equals("")) {
                        interestListLangEle.add(splitEle);
                    }
                }
            }
            if (interestFramework != null && !interestFramework.isEmpty()){
                String[] SplitInterestFramework = interestFramework.split("_");
                for (String splitEle:
                        SplitInterestFramework) {
                    if (splitEle != null &&!splitEle.equals("")) {
                        interestListFrameEle.add(splitEle);
                    }
                }
            }
            if (interestJob != null && !interestJob.isEmpty()) {
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
        model.addAttribute("interestListLang",interestListLang);
        model.addAttribute("interestListFrame",interestListFrame);
        model.addAttribute("interestListJob",interestListJob);

        return "meeting";
    }
}
