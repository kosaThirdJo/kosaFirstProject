package threestar.selectstar.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private int commentId;
    private int meetingId;
    private int userId;
    private String content;
    private LocalDateTime creationDate;
}
