package threestar.selectstar.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDTO {
	private String searchWord;
	private List<Integer> searchCategory;
	private List<String> searchLanguages;
	private List<String> searchFrameworks;
	private List<String> searchJobs;
}


