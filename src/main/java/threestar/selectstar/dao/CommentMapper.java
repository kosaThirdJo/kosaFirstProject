package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.MeetingVO;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("select * from comment where meeting_id= #{meetingId}")
    List<MeetingVO> getCommentListByMeetingId(int meetingId);
}
