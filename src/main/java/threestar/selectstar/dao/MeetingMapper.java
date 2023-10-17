package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    // 삭제기능 <--- 차후

    // 댓글 조회 해야 됨 필수

    // 댓글 추가 입력폼.. 필수 만약 댓글올렷으면 리다이렉트..

    // 신청 폼 <-- 차후?

    // 메인 - 최신글 조회 (ORDER BY) (현재는 4개만 출력)
    @Select("SELECT * FROM meeting ORDER BY creation_date DESC LIMIT 4")
    List<MeetingVO> getLatestMeetings();

    // 메인 - 인기글 조회 (RANK) : 최근 일주일간 올라온 글 중에서 조회수 높은 것 10개
    @Select("SELECT * FROM meeting WHERE DATEDIFF(NOW(), creation_date) <= 7 ORDER BY views DESC LIMIT 10")
    List<MeetingVO> getPopularMeetings();

    // 검색 - 모임글 검색 (제목 일치)
    @Select("SELECT * FROM meeting WHERE title LIKE CONCAT('%', #{searchWord}, '%')")
    List<MeetingVO> searchMeetings(@Param("searchWord") String searchWord);
}
