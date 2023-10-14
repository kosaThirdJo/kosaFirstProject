package threestar.selectstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import threestar.selectstar.dao.MeetingMapper;
import threestar.selectstar.domain.MeetingVO;

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
        return "meeting";
    }
}
