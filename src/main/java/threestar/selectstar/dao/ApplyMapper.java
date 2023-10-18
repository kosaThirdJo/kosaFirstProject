package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import threestar.selectstar.domain.ApplyVO;
import threestar.selectstar.domain.CommentDTO;

@Mapper
public interface ApplyMapper {
    @Insert("INSERT INTO apply (user_id, meeting_id, email_address, sns_address, reason, application_date) VALUES (#{userId}, #{meetingId}, #{emailAddress}, #{snsAddress}, #{reason}, #{applicationDate})")
    boolean insertComment(ApplyVO applyVo);

}