package threestar.selectstar.etc;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InterestMap{
    public HashMap<String, String> frameworkMap;
    public HashMap<String, String> jobMap;
    public HashMap<String, String> langMap;
    public InterestMap() {

        frameworkMap = new HashMap<>();
        jobMap = new HashMap<>();
        langMap = new HashMap<>();
        langMap.put("1", "Java");
        langMap.put("2", "Python");
        langMap.put("3", "C#");
        langMap.put("4", "C++");
        langMap.put("5", "JavaScript");
        langMap.put("6", "Ruby");
        langMap.put("7", "Swift");
        langMap.put("8", "TypeScript");
        langMap.put("9", "PHP");
        frameworkMap.put("1", "Spring");
        frameworkMap.put("2", "React");
        frameworkMap.put("3", "Angular");
        frameworkMap.put("4", "Vue.js");
        frameworkMap.put("5", "Express.js");
        frameworkMap.put("6", "Django");
        frameworkMap.put("7", "Ruby on Rails");
        frameworkMap.put("8", "Flask");
        frameworkMap.put("9", "Laravel");
        jobMap.put("1", "프론트엔드");
        jobMap.put("2", "백엔드");
        jobMap.put("3", "풀스택");
        jobMap.put("4", "모바일 앱 개발");
        jobMap.put("5","게임 개발");
        jobMap.put("6", "데이터베이스");
        jobMap.put("7", "데브옵스");
        jobMap.put("8", "디자이너");
        jobMap.put("9", "기획자");
    }
}
