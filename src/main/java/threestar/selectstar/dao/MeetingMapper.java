package threestar.selectstar.dao;

import org.apache.ibatis.annotations.*;
import threestar.selectstar.domain.CommentDTO;
import threestar.selectstar.domain.MeetingDTO;
import threestar.selectstar.domain.MeetingVO;

import java.util.List;

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

    // 댓글 조회 해야 됨 필수

    // 댓글 추가 입력폼.. 필수 만약 댓글올렷으면 리다이렉트..

    // 신청 폼 <-- 차후?


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
}
