package threestar.selectstar.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class UserVO {
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
