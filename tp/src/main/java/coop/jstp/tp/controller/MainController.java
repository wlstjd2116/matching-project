package coop.jstp.tp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.dao.MatchDAO;
import coop.jstp.tp.dao.MatchDAOImpl;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.MatchStartDTO;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
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

        MatchStartDTO mDto = new MatchStartDTO();
        mDto.setMatchingTier(matchTier);
        mDto.setUserNum(userNum);
        // 반환될 유저 names
        List<String> summonerNames = new ArrayList<>();

        // 매칭중인 사람들의 정보를 가져옴
        List<HashMap<?, ?>> matchingUserList = matchingService.getMatchedUserInfo();

        //매칭 중인 두 유저를 매칭
        var matchflag = false;

        // 추후 MatchDTO로 변경하기
        for(int i=0; i<matchingUserList.size(); i++){
            if((int)matchingUserList.get(i).get("MATCHING_TIER") == mDto.getMatchingTier()){
                log.info("매칭 중인 다른 사용자가 있습니다.");
                List<MatchingDTO> summonerList = matchingService.matchedUser();

                //매칭된 두 사람을 매칭 DB에서 제외
                for (MatchingDTO matchingDTO : summonerList) {
                    TestDTO summonerName = matchingService.getSummonerName(matchingDTO.getU_NUM());
                    TestDTO summonerName2 = matchingService.getSummonerName((mDto.getUserNum()));

                    // front로 반환 될 List
                    summonerNames.add(summonerName.getSummoner_Name());
                    summonerNames.add(summonerName2.getSummoner_Name());
                    matchingService.matchingEnd(matchingDTO.getU_NUM());

                }
                log.info("매칭 완료되었습니다.");
                matchflag = true;
                break;
            }
        }
        if(!matchflag){
            // 매칭 등록
            log.info("Matching start user : "+ mDto.getUserNum());
            matchingService.matchingStart(mDto);
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(summonerNames, header, HttpStatus.OK);
    }

    // 매칭 종료 api
    @RequestMapping("/api/match-off")
    public void matchingEnd(@RequestParam int userNum){
        matchingService.matchingEnd(userNum);
    }
}