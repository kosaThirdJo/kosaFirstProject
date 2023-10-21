package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.ApplyVO;

@Mapper
public interface ApplyMapper {
    @Insert("INSERT INTO apply (user_id, meeting_id, email_address, sns_address, reason, application_date) VALUES (#{userId}, #{meetingId}, #{emailAddress}, #{snsAddress}, #{reason}, #{applicationDate})")
    boolean insertComment(ApplyVO applyVo);
    @Select("select count(*) from apply where meeting_id = #{meetingId} group by meeting_id")
    Integer countApplyByMeetingId(int meetingId);
    @Select("select count(*) from apply where user_id = #{userId} and meeting_id = #{meetingId}")
    Integer countApplyByMeetingIDAndUserId(ApplyVO applyVO);

}
