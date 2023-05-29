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

        // 매칭 시작.
        try {
            matchingService.matchingStart(mDto);
            log.info(mDto.getUserNum() + ", 매칭 시작");
        } catch (DataIntegrityViolationException exception) {
            log.info(mDto.getUserNum() + ", 이미 매칭 중인 유저이거나 데이터 오류가 발생하였습니다. " + exception);
        }

        // 매칭중인 사람들의 정보를 가져옴
    //    List<MatchingDTO> matchingUserList = matchingService.getMatchedUserInfo();
        List<MatchingDTO> matchingUserList = matchingService.getMatchedUserInfo();
        System.out.println("################## test: "+matchingUserList.toString());
        System.out.println("################## test: "+matchingUserList.getClass().getName());

        //매칭 중인 두 유저를 매칭
        if (matchingUserList.size() >= 2) {
            for (MatchingDTO user : matchingUserList) {
                if (user.getMatching_Tier() == mDto.getMatchingTier()) {
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
            }
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(summonerNames, header, HttpStatus.OK);
    }

//    @GetMapping("/api/matching")
//    public ResponseEntity<?> matching(){
//        // 반환될 유저 names
//        List<String> summonerNames = new ArrayList<>();
//
//        // 매칭중인 사람들의 정보를 가져옴
//        List<MatchingDTO> matchingUserList= matchingService.getMatchedUserInfo();
//
//        //매칭 중인 두 유저를 매칭
////        if(matchingUserList != null && matchingService.matchingOther() >= 2){
////            for(MatchingDTO user : matchingUserList){
////                if(user.getMatching_Tier() == mDto.getMatchingTier()){
////                    log.info("매칭 중인 다른 사용자가 있습니다.");
////                    List<MatchingDTO> summonerList = matchingService.matchedUser();
////
////                    // 매칭된 두 사람을 매칭 DB에서 제외
////                    for (MatchingDTO matchingDTO : summonerList) {
////                        TestDTO summonerName = matchingService.getSummonerName(matchingDTO.getU_NUM());
////                        // front로 반환 될 List
////                        summonerNames.add(summonerName.getSummoner_Name());
////                        matchingService.matchingEnd(matchingDTO.getU_NUM());
////                    }
////
////                    log.info(summonerNames.toString());
////                    log.info("매칭 완료되었습니다.");
////                }
////            }
////        }
//
//        //매칭 중인 두 유저를 매칭
//        if(matchingUserList != null && matchingService.matchingOther() >= 2){
//            log.info("매칭 중인 다른 사용자가 있습니다.");
//
//            List<MatchingDTO> summonerList = matchingService.matchedUser();
//
//            int userCnt = matchingUserList.size();
//
//            for(int i=0; i<userCnt-1; i++){
//                for(int j=i+1; j<userCnt; j++){
//                    if(matchingUserList.get(i).getMatching_Tier() == matchingUserList.get(j).getMatching_Tier()){
//                        for (MatchingDTO matchingDTO : summonerList) {
//                            TestDTO summonerName = matchingService.getSummonerName(matchingDTO.getU_NUM());
//
//                            // front로 반환 될 List
//                            summonerNames.add(summonerName.getSummoner_Name());
//                            matchingService.matchingEnd(matchingDTO.getU_NUM());
//                        }
//
//                        matchingUserList.remove(matchingUserList.get(i));
//                        matchingUserList.remove(matchingUserList.get(j));
//                        userCnt -= 2;
//
//                        if(j > userCnt) j = userCnt;
//                        break;
//                    }
//                }
//            }
//        }
//
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
//
//        return new ResponseEntity<>(summonerNames, header, HttpStatus.OK);
//    }

    // 매칭 종료 api
    @RequestMapping("/api/match-off")
    public void matchingEnd(@RequestParam int userNum){
        matchingService.matchingEnd(userNum);
    }
}