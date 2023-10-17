package threestar.selectstar.domain;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class MeetingDTO {
    private int meetingId;
    private int userId;
    private String title;
    private int category;
    private int status;
    private LocalDateTime applicationDeadline;
    private int views;
    private int recruitmentCount;
    private int applicationCount;
    private String location;
    private String description;
    private LocalDateTime creationDate;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
}
