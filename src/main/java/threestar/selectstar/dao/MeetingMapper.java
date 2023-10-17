package threestar.selectstar.dao;
import org.apache.ibatis.annotations.*;

import threestar.selectstar.domain.MeetingDTO;
import threestar.selectstar.domain.MeetingVO;

import java.util.List;

@Mapper
public interface MeetingMapper {
    // 모든 미팅 조회 상태가 2(삭제면) 조회안함
    @Select("select meeting_id meetingId, user_id userId, title,category,status,application_deadline applicationDeadline,views,recruitment_count recruitmentCount,application_count applicationCount,location,description,creation_date creationDate,interest_language interestLanguage,interest_framework interestFramework,interest_job interestJob from meeting where status != 2 order by meeting_id desc")
    List<MeetingVO> getAllMeetingList();
    // 단건 미팅 조회 상태가 2(삭제면) 조회안함
    @Select("select meeting_id meetingId, user_id userId, title,category,status,application_deadline applicationDeadline,views,recruitment_count recruitmentCount,application_count applicationCount,location,description,creation_date creationDate,interest_language interestLanguage,interest_framework interestFramework,interest_job interestJob from meeting where meeting_id= #{meetingId} and status != 2")
    MeetingVO getMeetingArticleById(int meetingId);
    // 조회수 올리기!
    @Update("update meeting set views= #{views} where meeting_id=#{meetingId}")
    boolean updateMeetingCount(@Param("views") int views, @Param("meetingId") int meetingId);
    // 신청 폼
    @Insert("insert into meeting (user_id, title, category,status,application_deadline,views,recruitment_count,location,description,creation_date,interest_language,interest_framework,interest_job) VALUES (#{userId}, #{title}, #{category},#{status},#{applicationDeadline},#{views},#{recruitmentCount},#{location},#{description},#{creationDate},#{interestLanguage},#{interestFramework},#{interestJob})")
    boolean insertMeeting(MeetingDTO meetingDTO);
    // 모집 완료 기능, 삭제 기능
    @Update("update meeting set status= #{status} where meeting_id=#{meetingId}")
    boolean updateStatus(@Param("status") int status, @Param("meetingId") int meetingId);

    // 메인 - 최신글 조회 (ORDER BY) (현재는 4개만 출력)
    @Select("SELECT * FROM meeting ORDER BY creation_date DESC LIMIT 4")
    List<MeetingVO> getLatestMeetings();

    // 메인 - 인기글 조회 (RANK) : 최근 일주일간 올라온 글 중에서 조회수 높은 것 10개
    @Select("SELECT * FROM meeting WHERE DATEDIFF(NOW(), creation_date) <= 7 ORDER BY views DESC LIMIT 10")
    List<MeetingVO> getPopularMeetings();

    // 검색 - 모임글 검색 (제목 일치)
    @Select("SELECT * FROM meeting WHERE title LIKE CONCAT('%', #{searchWord}, '%')")
    List<MeetingVO> searchMeetings(@Param("searchWord") String searchWord);



    //마이페이지-내가 작성한 글목록 조회(제목, 분야, 모집상태, 장소, 조회수, 모집인원, 신청인원, 작성일, 모집마감일)
    @Select("select meeting_id meetingId, user_id userId, title, category, status, application_deadline applicationDeadline, " +
            "views, recruitment_count recruitmentCount, application_count applicationCount, " +
            "creation_date creationDate, location, description " +
            "from meeting where user_id= #{userId}")
    public List<MeetingVO> getMyMeetingList(int userId);
    
    //마이페이지-내가 신청한 글 목록 조회
}
