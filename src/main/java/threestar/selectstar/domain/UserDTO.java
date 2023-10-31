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
    private LocalDateTime joinDate;
    private byte[] profilePhoto;
    private String aboutMe;
    private String profileContent;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
}