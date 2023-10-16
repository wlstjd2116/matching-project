package coop.jstp.tp.controller;

import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RestController
public class MainController {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private MemberService memberService;

    // 매칭 시작하는 API
    @GetMapping("/api/match-on")
    public ResponseEntity<?> matchingStart(@RequestParam int userNum, @RequestParam int matchTier) {

        MatchStartDTO mDto = new MatchStartDTO(userNum, matchTier);

        //매칭 중인 두 유저를 매칭
        List<?> summonerNames = matchingService.matching(mDto);
        matchingService.setSummonerNames(summonerNames);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(summonerNames, header, HttpStatus.OK);
    }

    @GetMapping("/api/call-matching")
    public ResponseEntity<Map<String, List<?>>> callUserData(){
        Map<String, List<?>> matchedUsersList = new HashMap<>();
        List<MatchedUsersDTO> userList = matchingService.callSavedUserList();
        List<?> summonerNames = matchingService.getSummonerNames();
        //log.info("msg, summonerNames : "+summonerNames.toString());
        matchedUsersList.put("userList", userList);
        matchedUsersList.put("userNameList", summonerNames);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(matchedUsersList, header, HttpStatus.OK);
    }

    // 매칭 종료 api
    @RequestMapping("/api/match-off")
    public void matchingEnd(@RequestParam int userNum){
        matchingService.matchingEnd(userNum);
    }

    // 매칭 완료, 매칭된 대상들 DB에 저장
    @RequestMapping("/api/match-complete")
    public void matchingComplete(@RequestParam int matchingNumber){
        log.info("complete"+String.valueOf(matchingNumber));
        matchingService.matchingComplete(matchingNumber);
    }
}