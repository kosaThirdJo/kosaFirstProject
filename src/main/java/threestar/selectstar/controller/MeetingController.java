package threestar.selectstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import threestar.selectstar.dao.UserMapper;
import threestar.selectstar.domain.UserVO;

import java.util.List;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
    @Autowired
    UserMapper dao;
    @GetMapping("/detail")
    public String contentDetail(){

        return "meetingdetail";
    }
    @GetMapping("/listAll")
    public String listAll(){
        List<UserVO> allUserList = dao.getAllUserList();
        for (Object ele:
             allUserList) {
            System.out.println(ele);
        }
        return "";
    }
    ;

}
