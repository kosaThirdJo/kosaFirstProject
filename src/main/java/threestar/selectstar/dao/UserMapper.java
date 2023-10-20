package threestar.selectstar.dao;


import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;

import threestar.selectstar.domain.SearchDTO;
import threestar.selectstar.domain.UserDTO;
import threestar.selectstar.domain.UserVO;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    public List<UserVO> getAllUserList();

    //마이페이지 이력관리 조회
    @Select("select user_id userId, nickname, email, profile_photo, about_me, profile_content from user where user_id = #{userId}")
    public UserDTO getUserProfileInfo(int userId);

    // 회원 가입
    @Insert("INSERT INTO user (name, password, email, nickname, location1, interest_language, interest_framework, interest_job, join_date) "
            + "VALUES (#{name}, #{password}, #{email}, #{nickname}, #{location1}, #{interest_language}, #{interest_framework}, #{interest_job}, NOW())")
    public boolean signupUser(UserDTO dto);

    // 로그인
    @Select("SELECT user_id FROM user WHERE name = #{name} AND password = #{password}")
    public Integer loginUser(@Param("name") String name, @Param("password") String password);
    
    //마이페이지(이력관리) 수정
    @Update("update user set about_me= #{about_me}, profile_content= #{profile_content} where user_id= #{userId}")
    public boolean updateProfileInfo(UserDTO userDTO);

    //개인정보수정화면 조회
    @Select("select user_id userId, nickname, email, profile_photo, location1, location2, "
            + "interest_language, interest_framework, interest_job from user where user_id= #{userId}")
    public UserDTO getUserInfo(int userId);

    //개인정보 수정
    @Update("update user set password= #{password}, email= #{email}, nickname= #{nickname}, "
            +"location1= #{location1}, location2= #{location2}, interest_language= #{interest_language},"
            + "interest_framework= #{interest_framework}, interest_job= #{interest_job} where user_id= #{userId}")
    public boolean updateUserInfo(UserDTO userDTO);
    @Select("select name from user where user_id= #{userId}")
    public String getNameById(int userId);
    @Select("select * from user where user_id= #{userId}")
    public UserVO getProfilePhotoById(int userId);
    @Select("select user_id from user where name= #{name}")
    public int getIdByName(String name);

    // 검색 - 회원 닉네임 검색
    @Select("SELECT user_id userId, nickname, profile_photo, about_me "
        + "FROM user WHERE nickname LIKE CONCAT('%', #{searchWord}, '%')")
    List<UserVO> searchUser(SearchDTO search);

    //마이페이지-프로필 이미지(profile_photo) 수정
    @Update("update user set profile_photo= #{profile_photo} where user_id= #{userId}")
    public boolean updateUserProfileImg(@Param("userId") int userId,@Param("profile_photo") byte[] profile_photo);

    //다른 이용자 프로필 조회
    @Select("select nickname, email, profile_photo, about_me, profile_content from user where user_id = #{userId}")
    public UserDTO getProfileInfo(int userId);
}
