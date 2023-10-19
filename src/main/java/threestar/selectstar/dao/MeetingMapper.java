package threestar.selectstar.dao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import threestar.selectstar.domain.MeetingDTO;
import threestar.selectstar.domain.MeetingVO;
import threestar.selectstar.domain.SearchDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeetingMapper {
    // 모든 미팅 조회 차후 상태가 1이면 해당 조회안하기 구현...
    @Select("select * from meeting order by meeting_id desc")
    List<MeetingVO> getAllMeetingList();
    // 단건 미팅 조회 <-- 세션에 유저 아이디를 저장하고 같으면 버튼활성화? 해야 될 듯
    @Select("select * from meeting where meeting_id= #{meetingId}")
    MeetingVO getMeetingArticleById(int meetingId);
    // 조회수 올리기!
    @Update("update meeting set views= #{views} where meeting_id=#{meetingId}")
    boolean updateMeetingCount(@Param("views") int views, @Param("meetingId") int meetingId);
    // 신청 폼
    @Insert("insert into meeting (user_id, title, category,status,application_deadline,views,recruitment_count,location,description,creation_date,interest_language,interest_framework,interest_job) VALUES (#{userId}, #{title}, #{category},#{status},#{applicationDeadline},#{views},#{recruitmentCount},#{location},#{description},#{creationDate},#{interestLanguage},#{interestFramework},#{interestJob})")
    boolean insertMeeting(MeetingDTO meetingDTO);
    // 모집 완료 기능

    // 삭제기능 <--- 차후
    // 신청 폼 <-- 차후?

    // 메인 - 최신글 조회 (ORDER BY) (현재는 4개만 출력)
    @Select("SELECT * FROM meeting ORDER BY creation_date DESC LIMIT 4")
    List<MeetingVO> getLatestMeetings();

    // 메인 - 인기글 조회 (RANK) : 최근 일주일간 올라온 글 중에서 조회수 높은 것 10개
    @Select("SELECT meeting_id meetingId, title FROM meeting WHERE DATEDIFF(NOW(), creation_date) <= 7 ORDER BY views DESC LIMIT 10")
    List<MeetingVO> getPopularMeetings();

    // 검색 - 모임글 검색 (제목 일치)
    @Select("SELECT meeting_id meetingId, title, category, status, application_deadline applicationDeadline, application_count applicationCount, location "
        + "FROM meeting WHERE title LIKE CONCAT('%', #{searchWord}, '%')")
    List<MeetingVO> searchMeetings(SearchDTO search);

    // 검색 - 모임글 검색 (필터링 적용)
    @Select({
        "<script>",
        "SELECT meeting_id meetingId, title, category, status, application_deadline applicationDeadline,",
        "application_count applicationCount, location",
        "FROM meeting",
        "WHERE title LIKE CONCAT('%', #{searchWord}, '%')",
        "<if test='searchCategory != null and !searchCategory.isEmpty()'>",
        "   AND category IN",
        "   <foreach collection='searchCategory' item='ct' open='(' separator=',' close=')'>",
        "       #{ct}",
        "   </foreach>",
        "</if>",
        "<if test='searchLanguages != null and !searchLanguages.isEmpty()'>",
        "   AND (",
        "       <foreach collection='searchLanguages' item='lang' separator=' OR '>",
        "           interest_language LIKE CONCAT('%_', #{lang}, '_%')",
        "       </foreach>",
        "   )",
        "</if>",
        "<if test='searchFrameworks != null and !searchFrameworks.isEmpty()'>",
        "   AND (",
        "       <foreach collection='searchFrameworks' item='fw' separator=' OR '>",
        "           interest_framework LIKE CONCAT('%_', #{fw}, '_%')",
        "       </foreach>",
        "   )",
        "</if>",
        "<if test='searchJobs != null and !searchJobs.isEmpty()'>",
        "   AND (",
        "       <foreach collection='searchJobs' item='job' separator=' OR '>",
        "           interest_job LIKE CONCAT('%_', #{job}, '_%')",
        "       </foreach>",
        "   )",
        "</if>",
        "</script>"
    })
    List<MeetingVO> selectMeetingsByFilter(SearchDTO search);

    // 댓글 조회 해야 됨 필수

    // 댓글 추가 입력폼.. 필수 만약 댓글올렷으면 리다이렉트..

    // 신청 폼 <-- 차후?


    //마이페이지-내가 작성한 글목록 조회(제목, 분야, 모집상태, 장소, 조회수, 모집인원, 신청인원, 작성일, 모집마감일)
    @Select("select meeting_id meetingId, user_id userId, title, category, status, application_deadline applicationDeadline, " +
            "views, recruitment_count recruitmentCount, application_count applicationCount, " +
            "creation_date creationDate, location, description " +
            "from meeting where user_id= #{userId}")
    public List<MeetingVO> getMyMeetingList(int userId);

    //마이페이지-내가 신청한 글 목록 조회
}
