package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("select * from comment where meeting_id= #{meetingId}")
    List<CommentDTO> getCommentListByMeetingId(String meetingId);
    @Insert("INSERT INTO comment (meeting_id, user_id, content, creation_date) VALUES (#{meetingId}, #{userId}, #{content}, #{creationDate})")
    boolean insertComment(CommentDTO commentDTO);


}
