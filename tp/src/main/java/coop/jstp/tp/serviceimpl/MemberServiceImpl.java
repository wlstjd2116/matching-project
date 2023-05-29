package coop.jstp.tp.serviceimpl;

import coop.jstp.tp.dao.MemberDAO;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.LoginDTO;
import coop.jstp.tp.vo.TestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class MemberServiceImpl implements MemberService {

    @Value("${riot.apikey}")
    private String apiKey;

    @Autowired
    private MemberDAO memberDAO;

    @Override
    public void memberInput(LoginDTO dto) {
        memberDAO.memberInput(dto);
    }
    @Override
    public ResponseEntity<?> idDupChk(TestDTO dto) {
        int chk = memberDAO.idDupChk(dto);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(chk, header, HttpStatus.OK);
    }

    // 로그인 시도
    @Override
    public TestDTO login(TestDTO dto) {
        TestDTO result = memberDAO.login(dto);
        return result;
    }

    // 회원가입 set parameter
    @Override
    public LoginDTO setInputParam(LoginDTO dto) {
        LoginDTO lDto = new LoginDTO();

        lDto.setId(dto.getId());
        lDto.setPw(dto.getPw());
        lDto.setUserName(dto.getUserName());

        return lDto;
    }

    // 유저의 암호화 된 값을 불러오는 서비스 메서드
    @Override
    public ResponseEntity<HashMap> getUserId(String summonerName) {

        // 통신에 필요한 라이브러리, 타입 등 선언
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> requestMessage = new HttpEntity<>(body, headers);

        // URI builder
        String url ="https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + apiKey;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        // uri Request -> responseEntity
        ResponseEntity<HashMap> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestMessage, HashMap.class);

        return response;
    }

    // 호출된 accountID를 통해서 해당 유저의 tier를 가져오는 함수
    @Override
    public ResponseEntity<ArrayList> getUserTier(String accountId) {

        // 통신에 필요한 라이브러리, 타입 등 선언
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> requestMessage = new HttpEntity<>(body, headers);

        // URI builder
        String url ="https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + accountId + "?api_key=" + apiKey;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        // uri Request -> responseEntity
        ResponseEntity<ArrayList> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestMessage, ArrayList.class);

        return response;
    }
}
