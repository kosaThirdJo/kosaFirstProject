package threestar.selectstar.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import threestar.selectstar.domain.UserDTO;
import threestar.selectstar.domain.UserVO;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<UserVO> getAllUserList();

    @Select("select nickname, profile_photo, about_me, profile_content from user where user_id = #{userId}")
    public UserDTO getUserProfileInfo(int userId);
}
