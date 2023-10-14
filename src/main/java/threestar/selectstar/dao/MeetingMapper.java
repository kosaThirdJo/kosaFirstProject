package threestar.selectstar.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.MeetingVO;

import java.util.List;

@Mapper
public interface MeetingMapper {
    @Select("select * from meeting")
    List<MeetingVO> getAllMeetingList();
}
