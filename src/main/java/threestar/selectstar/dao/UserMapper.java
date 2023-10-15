package threestar.selectstar.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import threestar.selectstar.domain.UserDTO;
import threestar.selectstar.domain.UserVO;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    public List<UserVO> getAllUserList();

    // 회원 가입
    @Insert("insert into user (name, password, email, nickname, location1, interest_language, interest_framework, interest_job, join_date) "
        + "values (#{name}, #{password}, #{email}, #{nickname}, #{location1}, #{interest_language}, #{interest_framework}, #{interest_job}, now())")
    public boolean signupUser(UserDTO dto);

    // 로그인
    @Select("select name, password from user where name = #{name} and password = #{password}")
    public UserDTO loginUser(UserDTO dto);


    //마이페이지(이력관리) 조회
    @Select("select user_id userId, nickname, email, profile_photo, about_me, profile_content from user where user_id= #{userId}")
    public UserDTO getUserProfileInfo(int userId);

    //마이페이지(이력관리) 수정
    @Update("update user set about_me= #{about_me}, profile_content= #{profile_content} where user_id= #{userId}")
    public boolean updateProfileInfo(UserDTO userDTO);

}
