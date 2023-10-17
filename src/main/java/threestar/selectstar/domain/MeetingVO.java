package threestar.selectstar.domain;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MeetingVO {
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
