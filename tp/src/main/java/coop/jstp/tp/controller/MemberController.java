package coop.jstp.tp.controller;

import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.LoginDTO;
import coop.jstp.tp.vo.TestDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.xml.ws.Response;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;


@Slf4j
@RequestMapping("/api")
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MatchingService matchingService;

    @ApiOperation(value = "회원가입을 정보를 받아 DB로 전송하는 API")
    @PostMapping("/memberInput")
    public void memberInput(@RequestBody LoginDTO dto){

        // DTO set Parameters
        LoginDTO lDto = memberService.setInputParam(dto);

        memberService.memberInput(lDto);
    }

    @ApiOperation(value = "id 값이 중복되어 있는지 체크하는 API, 1 이상이 리턴된다면 아이디가 있다는 것을 의미함.")
    @RequestMapping(value = "/idDupChk", method = RequestMethod.GET)
    public ResponseEntity<?> idDupChk(@RequestParam String id){

        TestDTO dto = new TestDTO();

        dto.setId(id);

        ResponseEntity<?> result = memberService.idDupChk(dto);

        return result;
    }

    @ApiOperation(value = "로그인 시도 API, ")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String pw){
        TestDTO dto = new TestDTO();

        dto.setId(id);
        dto.setPw(pw);

        TestDTO returnVal = memberService.login(dto);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(returnVal, header, HttpStatus.OK);
    }

    @GetMapping("/user-tier")
    public ResponseEntity<?> getTier(@RequestParam String summonerName) throws IOException {

        // 유저 정보 받아서 티어 정보로 변환
        ResponseEntity<HashMap> idd = memberService.getUserId(summonerName);

            ResponseEntity<ArrayList> respon;
        try{
            respon = memberService.getUserTier(Objects.requireNonNull(idd.getBody()).get("id").toString());
        }catch (Exception e){
            log.info("존재하지 않는 소환사 이름입니다.");

            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

            return new ResponseEntity<>("존재하지 않는 소환사 이름입니다", header, HttpStatus.OK);
        }


        // 티어 정보
        HashMap<?, ?> ContainTierInfoHashMap = (HashMap) Objects.requireNonNull(respon.getBody()).get(0);

        // 솔로랭크가 아니라면
        if(!ContainTierInfoHashMap.get("queueType").equals("RANKED_SOLO_5x5")){
            ContainTierInfoHashMap = (HashMap) Objects.requireNonNull(respon.getBody()).get(1);
        };

        log.info(ContainTierInfoHashMap.toString());
        log.info(ContainTierInfoHashMap.get("tier").toString() + ContainTierInfoHashMap.get("rank").toString());


        // 티어를 String 형식으로 반환
        HashMap<String, Object> hashTier = new HashMap<>();
        String strTier = ContainTierInfoHashMap.get("tier").toString() + ContainTierInfoHashMap.get("rank").toString();
        // 매칭용 티어
        int matchingTier = matchingService.tierList(strTier);
        hashTier.put("tier",ContainTierInfoHashMap.get("tier").toString());
        hashTier.put("rank", ContainTierInfoHashMap.get("rank").toString());
        hashTier.put("matchingTier", matchingTier);

        System.out.println(matchingTier);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(hashTier, header, HttpStatus.OK);
    }
}
