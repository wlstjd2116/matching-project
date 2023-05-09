package coop.jstp.tp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/api")
    public List test3(){
        return Arrays.asList("H","I!!");
    }

    @RequestMapping("/api/search")
    public ResponseEntity<?> test(@RequestParam String summonerName) throws JsonProcessingException {
        System.out.println("test");
        ResponseEntity<?> result = matchingService.getUserInfo(summonerName);
        return result;
    }

    @RequestMapping("/match-on")
    public void matchingStart(@RequestParam int userNum){
        try {
            matchingService.matchingStart(userNum);
            System.out.println(userNum + ", 매칭 시작");
        }catch (Exception e){
            System.out.println("이미 매칭 중입니다.");
        }
    }

    @RequestMapping("/match-off")
    public void matchingEnd(@RequestParam int userNum){
        try{
            matchingService.matchingEnd(userNum);
            System.out.println(userNum + ", 매칭 중단");
        }catch(Exception e){
            System.out.println("비정상적인 요청입니다.");
        }
    }


}
