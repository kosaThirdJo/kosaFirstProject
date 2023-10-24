package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.ApplyVO;

import java.util.List;

@Mapper
public interface ApplyMapper {
    @Insert("INSERT INTO apply (user_id, meeting_id, email_address, sns_address, reason, application_date) VALUES (#{userId}, #{meetingId}, #{emailAddress}, #{snsAddress}, #{reason}, #{applicationDate})")
    boolean insertComment(ApplyVO applyVo);
    @Select("SELECT count(*) FROM apply where meeting_id = #{meetingId} group by meeting_id")
    Integer countApplyByMeetingId(int meetingId);
    @Select("SELECT count(*) FROM apply where user_id = #{userId} and meeting_id = #{meetingId}")
    Integer countApplyByMeetingIDAndUserId(ApplyVO applyVO);

    @Select("SELECT u.nickname  FROM apply a join user u on a.user_id = u.user_id WHERE a.meeting_id = #{meetingId}")
    List<String> selectAllByMeetingId(Integer meetingId);
}
