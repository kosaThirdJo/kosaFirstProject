package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommentMapper {
    // id로 댓글 리스트 조회
    @Select("select * from comment where meeting_id= #{meetingId}")
    List<CommentDTO> getCommentListByMeetingId(int meetingId);
    // 댓글 작성
    @Insert("INSERT INTO comment (meeting_id, user_id, content, creation_date) VALUES (#{meetingId}, #{userId}, #{content}, #{creationDate})")
    boolean insertComment(CommentDTO commentDTO);
    // 댓글 개수 세기
    @Select("select count(comment_id) as count from comment where meeting_id = #{meeting_id}")
    int calcCommentCount(int meeting_id);
}
