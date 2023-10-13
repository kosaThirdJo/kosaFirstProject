package threestar.selectstar.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ApplyVO {
    private int userId;
    private int meetingId;
    private String emailAddress;
    private String snsAddress;
    private LocalDateTime applicationDate;

}
