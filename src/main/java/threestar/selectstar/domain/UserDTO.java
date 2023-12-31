package threestar.selectstar.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDTO {
    private int userId;
    private String name;
    private String password;
    private String email;
    private String nickname;
    private String location1;
    private String location2;
    private LocalDateTime join_date;
    private byte[] profile_photo;
    private String about_me;
    private String profile_content;
    private String interest_language;
    private String interest_framework;
    private String interest_job;
}
