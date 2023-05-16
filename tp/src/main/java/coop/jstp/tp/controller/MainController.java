package coop.jstp.tp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.dao.MatchDAO;
import coop.jstp.tp.dao.MatchDAOImpl;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class MainController {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private MemberService memberService;


    @GetMapping("/test")
    public void test3(){
        String a = matchingService.matchedUser().toString();
        log.info("a: " + a);
    }
    @RequestMapping("/api/search")
    public ResponseEntity<?> test(@RequestParam String summonerName) throws JsonProcessingException {
        System.out.println("test");
        ResponseEntity<?> result = matchingService.getUserInfo(summonerName);
        log.info("log실행?");
        return result;
    }

    // 매칭 시작하는 API
    @RequestMapping("/api/match-on")
    public ResponseEntity<?> matchingStart(@RequestParam int userNum) {
        // 매칭 시작.
        try {
            matchingService.matchingStart(userNum);
            log.info(userNum+", 매칭 시작");
        }catch (DataIntegrityViolationException exception){
            log.info(userNum+", 이미 매칭 중인 유저입니다."+ exception);
        }
        List<String> summonerNames = new ArrayList<>();

        if(matchingService.matchingOther() >= 2){
            log.info("매칭 중인 다른 사용자가 있습니다.");
            List<MatchingDTO> summonerList = matchingService.matchedUser();

            // 매칭된 두 사람을 매칭 DB에서 제외
            for (MatchingDTO matchingDTO : summonerList) {
                TestDTO summonerName = matchingService.getSummonerName(matchingDTO.getU_NUM());
                // front로 반환 될 List
                summonerNames.add(summonerName.getSummoner_Name());
                matchingService.matchingEnd(matchingDTO.getU_NUM());
            }

            log.info(summonerNames.toString());
            log.info("매칭 완료되었습니다.");

        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(summonerNames, header, HttpStatus.OK);

    }

    @RequestMapping("/api/ㅣmatch-off")
    public void matchingEnd(@RequestParam int userNum){
        matchingService.matchingEnd(userNum);
    }
}
