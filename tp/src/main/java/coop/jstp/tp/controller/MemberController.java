package coop.jstp.tp.controller;

import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.TestDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
public class MemberController {


    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "회원가입을 정보를 받아 DB로 전송하는 API")
    @RequestMapping(value = "/api/memberInput", method = RequestMethod.POST)
    public void memberInput(@RequestParam String id, @RequestParam String pw, @RequestParam String userName){
        TestDTO dto = new TestDTO();

        dto.setId(id);
        dto.setPw(pw);
        dto.setSummoner_Name(userName);

        log.info(dto.getSummoner_Name());

        memberService.memberInput(dto);
    }

    @ApiOperation(value = "id 값이 중복되어 있는지 체크하는 API, 1 이상이 리턴된다면 아이디가 있다는 것을 의미함.")
    @RequestMapping(value = "/api/idDupChk", method = RequestMethod.GET)
    public ResponseEntity<?> idDupChk(@RequestParam String id){

        TestDTO dto = new TestDTO();

        dto.setId(id);

        ResponseEntity<?> result = memberService.idDupChk(dto);

        return result;
    }

    @ApiOperation(value = "로그인 시도 API, ")
    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String pw){
        TestDTO dto = new TestDTO();

        dto.setId(id);
        dto.setPw(pw);

        TestDTO returnVal = memberService.login(dto);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(returnVal, header, HttpStatus.OK);
    }
}
