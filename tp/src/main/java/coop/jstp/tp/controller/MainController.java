package coop.jstp.tp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.dao.MatchDAO;
import coop.jstp.tp.dao.MatchDAOImpl;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.plaf.synth.SynthTreeUI;
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

    // 매칭 시작하는 API
    @GetMapping("/api/match-on")
    public ResponseEntity<?> matchingStart(@RequestParam int userNum, @RequestParam int matchTier) {

        MatchStartDTO mDto = new MatchStartDTO(userNum, matchTier);

        //매칭 중인 두 유저를 매칭
        List<?> summonerNames = matchingService.matching(mDto);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(summonerNames, header, HttpStatus.OK);
    }

    @GetMapping("/api/call-matching")
    public ResponseEntity<List<MatchedUsersDTO>> callUserData(){
        List<MatchedUsersDTO> userList = matchingService.callSavedUserList();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        log.info(userList.toString());
        return new ResponseEntity<>(userList, header, HttpStatus.OK);
    }

    // 매칭 종료 api
    @RequestMapping("/api/match-off")
    public void matchingEnd(@RequestParam int userNum){
        matchingService.matchingEnd(userNum);
    }

    @RequestMapping("/api/match-complete")
    public void matchingComplete(@RequestParam int matchingNumber){
        log.info("complete"+String.valueOf(matchingNumber));
        matchingService.matchingComplete(matchingNumber);
    }
}