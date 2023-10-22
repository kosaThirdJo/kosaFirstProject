package threestar.selectstar.dao;

import org.apache.ibatis.annotations.*;

import threestar.selectstar.domain.MeetingDTO;
import threestar.selectstar.domain.MeetingVO;
import threestar.selectstar.domain.SearchDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeetingMapper {
    // 모든 미팅 조회 상태가 2(삭제면) 조회안함 , 10글자 이상시 생략 후 ... 붙임
    @Select("select meeting_id meetingId, user_id userId, if(CHAR_LENGTH(title) > 10,concat(substr(title,1,10),'...'),title) as title, category, status, application_deadline applicationDeadline, views, recruitment_count recruitmentCount, application_count applicationCount, location, description, creation_date creationDate,interest_language interestLanguage,interest_framework interestFramework,interest_job interestJob from meeting where is_delete = 0 order by ${order} desc limit 12 offset #{offset}")
    List<MeetingVO> getAllMeetingList(@Param("offset")int offset,@Param("order") String order);
    @Select("select meeting_id meetingId, user_id userId, if(CHAR_LENGTH(title) > 10,concat(substr(title,1,10),'...'),title) as title, category, status, application_deadline applicationDeadline, views, recruitment_count recruitmentCount, application_count applicationCount, location, description, creation_date creationDate,interest_language interestLanguage,interest_framework interestFramework,interest_job interestJob from meeting where is_delete = 0 and category= #{category} order by ${order} desc limit 12 offset #{offset}")
    List<MeetingVO> getAllMeetingListByCategory(@Param("offset") int offset, @Param("category") int category,@Param("order")String order);
    @Select("select count(*) from meeting where is_delete = 0")
    int getAllMeetingCountList();
    @Select("select count(*) from meeting where is_delete = 0 and category=#{category}")
    int getAllMeetingCountListByCategory(@Param("category") int category);
    // 카테고리 별로 모든 미팅 조회

    // 단건 미팅 조회 상태가 2(삭제면) 조회안함
    @Select("select meeting_id meetingId, user_id userId, title,category,status,application_deadline applicationDeadline,views,recruitment_count recruitmentCount,application_count applicationCount,location,description,creation_date creationDate,interest_language interestLanguage,interest_framework interestFramework,interest_job interestJob from meeting where meeting_id= #{meetingId} and is_delete = 0")
    MeetingDTO getMeetingArticleById(int meetingId);
    // 조회수 올리기!
    @Update("update meeting set views= #{views} where meeting_id=#{meetingId}")
    boolean updateMeetingCount(@Param("views") int views, @Param("meetingId") int meetingId);
    // 신청 폼
    @Insert("insert into meeting (user_id, title, category,status,application_deadline,views,recruitment_count,location,description,creation_date,interest_language,interest_framework,interest_job) VALUES (#{userId}, #{title}, #{category},#{status},#{applicationDeadline},#{views},#{recruitmentCount},#{location},#{description},#{creationDate},#{interestLanguage},#{interestFramework},#{interestJob})")
    boolean insertMeeting(MeetingDTO meetingDTO);
    // 모집 완료 기능
    @Update("update meeting set status= #{status} where meeting_id=#{meetingId}")
    boolean updateStatus(@Param("status") int status, @Param("meetingId") int meetingId);
    // 삭제 기능
    @Update("update meeting set is_delete= 1 where meeting_id=#{meetingId}")
    boolean deleteMeeting(@Param("meetingId") int meetingId);
    // 메인 - 최신글 조회 (ORDER BY) (현재는 4개만 출력)
    @Select(
        "SELECT M.meeting_id meetingId, M.title, M.status, M.location, M.application_deadline applicationDeadline, M.application_count applicationCount, M.views, COUNT(C.comment_id) commentCount "
        + "FROM meeting M LEFT JOIN comment C ON M.meeting_id = C.meeting_id "
        + "WHERE M.is_delete = 0 GROUP BY M.meeting_id ORDER BY M.creation_date DESC LIMIT 4")
    List<MeetingVO> getLatestMeetings();

    // 메인 - 인기글 조회 (RANK) : 최근 일주일간 올라온 글 중에서 조회수 높은 것 10개
    @Select("SELECT meeting_id meetingId, title FROM meeting "
        + "WHERE DATEDIFF(NOW(), creation_date) <= 7 AND is_delete = 0 "
        + "ORDER BY views DESC LIMIT 10")
    List<MeetingVO> getPopularMeetings();

    // 검색 - 모임글 검색 (제목 일치)
    @Select("SELECT meeting_id meetingId, title, category, status, application_deadline applicationDeadline, application_count applicationCount, location "
        + "FROM meeting WHERE title LIKE CONCAT('%', #{searchWord}, '%') AND is_delete = 0 "
        + "ORDER BY creation_date DESC")
    List<MeetingVO> searchMeetings(SearchDTO search);

    // 검색 - 모임글 검색 (필터링 적용)
    @Select({
        "<script>",
        "SELECT meeting_id meetingId, title, category, status, application_deadline applicationDeadline,",
        "application_count applicationCount, location",
        "FROM meeting",
        "WHERE title LIKE CONCAT('%', #{searchWord}, '%') AND is_delete = 0 " ,
        "<if test='searchCategory != null and !searchCategory.isEmpty()'>",
        "   AND category IN",
        "   <foreach collection='searchCategory' item='ct' open='(' separator=',' close=')'>",
        "       #{ct}",
        "   </foreach>",
        "</if>",
        "<if test='searchLanguages != null and !searchLanguages.isEmpty()'>",
        "   AND (",
        "       <foreach collection='searchLanguages' item='lang' separator=' OR '>",
        "           interest_language LIKE CONCAT('%', #{lang}, '%')",
        "       </foreach>",
        "   )",
        "</if>",
        "<if test='searchFrameworks != null and !searchFrameworks.isEmpty()'>",
        "   AND (",
        "       <foreach collection='searchFrameworks' item='fw' separator=' OR '>",
        "           interest_framework LIKE CONCAT('%', #{fw}, '%')",
        "       </foreach>",
        "   )",
        "</if>",
        "<if test='searchJobs != null and !searchJobs.isEmpty()'>",
        "   AND (",
        "       <foreach collection='searchJobs' item='job' separator=' OR '>",
        "           interest_job LIKE CONCAT('%', #{job}, '%')",
        "       </foreach>",
        "   )",
        "</if>",
        " ORDER BY creation_date DESC",
        "</script>"
    })
    List<MeetingVO> selectMeetingsByFilter(SearchDTO search);

    //마이페이지-내가 작성한 글목록 조회(제목, 분야, 모집상태, 장소, 조회수, 모집인원, 신청인원, 작성일, 모집마감일)
    @Select("select meeting_id meetingId, user_id userId, title, category, status, application_deadline applicationDeadline, " +
            "views, recruitment_count recruitmentCount, application_count applicationCount, " +
            "creation_date creationDate, location, description " +
            "from meeting where user_id= #{userId} and is_delete = 0 " +
            "order by creationDate desc")
    public List<MeetingVO> getMyMeetingList(int userId);

    //마이페이지-내가 작성한 글목록 카테고리별 조회
    @Select("select meeting_id meetingId, user_id userId, title, category, status, application_deadline applicationDeadline, " +
            "views, recruitment_count recruitmentCount, application_count applicationCount, " +
            "creation_date creationDate, location, description " +
            "from meeting where user_id= #{userId} and is_delete = 0 and category= #{category} " +
            "order by creationDate desc")
    public List<MeetingVO> getMyMeetingListByCategory(@Param("userId") int userId, @Param("category") int category);


    //마이페이지-내가 신청한 글 목록 조회
    @Select("select m.meeting_id meetingId, m.title, m.is_delete isDelete, m.category, m.status, " +
            "m.application_deadline applicationDeadline, m.views, m.recruitment_count recruitmentCount, " +
            "m.application_count applicationCount, m.creation_date creationDate, m.location, m.description " +
            "from meeting m " +
            "join apply a " +
            "on m.meeting_id = a.meeting_id " +
            "where a.user_id= #{userId}  and m.is_delete = 0;")
    public List<MeetingVO> getMyApplyList(int userId);
    // 신청시 신청인원 수정
    @Update("update meeting set application_count = #{applicationCount} where meeting_id=#{meetingId}")
    public boolean updateApplicationCount(MeetingDTO meetingDTO);
    @Update("update meeting set title = #{title}, category = #{category}, application_deadline = #{applicationDeadline},recruitment_count = #{recruitmentCount},location = #{location},description = #{description},creation_date = #{creationDate},interest_language = #{interestLanguage},interest_framework = #{interestFramework},interest_job = #{interestJob} where meeting_id=#{meetingId}")
    public boolean updateMeetingById(MeetingDTO meetingDTO);

}
