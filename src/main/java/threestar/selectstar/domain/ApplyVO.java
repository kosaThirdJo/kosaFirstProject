package threestar.selectstar.domain;

import lombok.*;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplyVO {
    private Integer userId;
    private Integer meetingId;
    private String emailAddress;
    private String snsAddress;
    private String reason;
    private LocalDateTime applicationDate;
}
