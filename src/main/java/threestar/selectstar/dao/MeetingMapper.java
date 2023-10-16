package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import threestar.selectstar.domain.MeetingVO;

import java.util.List;

@Mapper
public interface MeetingMapper {
    // 모든 미팅 조회 차후 상태가 1이면 해당 조회안하기 구현...
    @Select("select * from meeting")
    List<MeetingVO> getAllMeetingList();
    // 단건 미팅 조회 <-- 세션에 유저 아이디를 저장하고 같으면 버튼활성화? 해야 될 듯
    @Select("select * from meeting where meeting_id= #{meetingId}")
    MeetingVO getMeetingArticleById(int meetingId);
    // 조회수 올리기!
    @Update("update meeting set views= #{views} where meeting_id=#{meetingId}")
    boolean updateMeetingCount(@Param("views") int views, @Param("meetingId") int meetingId);
    // 삭제기능 <--- 차후

    // 신청 폼 <-- 차후?
}
