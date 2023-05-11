package coop.jstp.tp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.dao.MatchDAO;
import coop.jstp.tp.dao.MatchDAOImpl;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class MainController {

    @Autowired
    private MatchingService matchingService;

    @RequestMapping("/test-page")
    public String testPage(){
        System.out.println("############################");
        return "/index";
    }

    @RequestMapping("/memberInput")
    public void memberInput(@RequestParam String id, @RequestParam String pw, @RequestParam String userName){
        TestDTO dto = new TestDTO();

        dto.setId(id);
        dto.setPw(pw);
        dto.setSummoner_Name(userName);
        log.info(dto.getSummoner_Name());
        matchingService.memberInput(dto);

    }

    @GetMapping("/test")
    public void test3(){
        String a = matchingService.matchedUser().toString();
        log.info("a: " + a);
    }

//    @RequestMapping("/api/search")
//    public ResponseEntity<?> test(@RequestParam String summonerName) throws JsonProcessingException {
//        System.out.println("test");
//        //ResponseEntity<?> result = matchDAO.getUserInfo(summonerName);
//        log.info("log실행?");
//        return result;
//    }

    @RequestMapping("/match-on")
    public void matchingStart(@RequestParam int userNum){
        // 매칭을 시작.
        try {
            matchingService.matchingStart(userNum);
            log.info(userNum+", 매칭 시작");
        }catch (Exception e){
            log.info("이미 매칭 중이거나, 등록되지 않은 사용자입니다."+e);
        }

        if(matchingService.matchingOther() >= 2){
            log.info("매칭 중인 다른 사용자가 있습니다.");
            List<MatchingDTO> summonerList = matchingService.matchedUser();

            // 매칭된 두 사람을 매칭 DB에서 제외
            for(int i=0; i<summonerList.size(); i++){
                matchingService.matchingEnd(summonerList.get(i).getU_NUM());
            }
            log.info("매칭 완료되었습니다.");
        }
    }

    @RequestMapping("/match-off")
    public void matchingEnd(@RequestParam int userNum){
        try{
            matchingService.matchingEnd(userNum);
            log.info(userNum+", 매칭 중단");
        }catch(Exception e){
            log.info("비정상적인 실행입니다."+e);
        }
    }
}
