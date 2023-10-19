package threestar.selectstar.domain;

import lombok.*;

import java.time.LocalDateTime;


@Builder
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplyVO {
    private int userId;
    private int meetingId;
    private String emailAddress;
    private String snsAddress;
    private String reason;
    private LocalDateTime applicationDate;
}
