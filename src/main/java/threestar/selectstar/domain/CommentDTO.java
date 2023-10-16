package threestar.selectstar.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private int commentId;
    private int meetingId;
    private int userId;
    private String content;
    private LocalDateTime creationDate;
}
