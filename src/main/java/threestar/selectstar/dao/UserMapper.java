package threestar.selectstar.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    // 회원 가입
    @Insert("insert into user (name, password, email, nickname, location1, interest_language, interest_framework, interest_job, join_date) "
        + "values (#{name}, #{password}, #{email}, #{nickname}, #{location1}, #{interest_language}, #{interest_framework}, #{interest_job}, now())")
    public boolean signupUser(UserDTO dto);

    // 로그인
    @Select("select user_id from user where name = #{name} and password = #{password}")
    public Integer loginUser(@Param("name") String name, @Param("password") String password);

}
