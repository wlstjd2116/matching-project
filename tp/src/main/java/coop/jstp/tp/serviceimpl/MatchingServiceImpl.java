package coop.jstp.tp.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import coop.jstp.tp.dao.MatchDAO;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.vo.MatchStartDTO;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("service")
public class MatchingServiceImpl implements MatchingService {

    @Value("${riot.apikey}")
    private String apiKey;

    @Autowired
    private MatchDAO matchDAO;

    @Override
    public ResponseEntity getUserInfo(String userName) throws JsonProcessingException {

        log.info(apiKey);

        // 통신에 필요한 라이브러리, 타입 등 선언
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> requestMessage = new HttpEntity<>(body, headers);

        // URI builder
//        String api_key = "RGAPI-e0d8fbf6-ef95-4ba5-ab3f-80a8ae6ebaaf"; // 외부 노출 금지
        String url ="https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + userName + "?api_key=" + apiKey;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        // uri Request -> responseEntity
        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestMessage, String.class);

        return response;
    }

    // 매칭 시작
    @Override
    public void matchingStart(MatchStartDTO dto) {
        matchDAO.matchingStart(dto);
    }

    // 매칭 중단
    @Override
    public void matchingEnd(int userNum) {
        matchDAO.matchingEnd(userNum);
    }

    @Override
    public int matchingOther() {
        return matchDAO.matchingOther();
    }

    @Override
    public List<MatchingDTO> matchedUser() {
        return matchDAO.matchedUser();
    }

    @Override
    public TestDTO getSummonerName(int uNum) {
        return matchDAO.getSummonerName(uNum);
    }

    @Override
    public int tierList(String tier) {
        //HashMap<String, Integer> tierHash = new HashMap<>();
        String[] tierArr = new String[] {"IRONI", "IRONII", "IRONIII", "IRONIV",
                "BRONZEI", "BRONZEII", "BRONZEIII", "BRONZEIV",
                "SILVERI", "SILVERII", "SILVERIII", "SILVERIV",
                "GOLDI", "GOLDII", "GOLDIII", "GOLDIV",
                "PLATINUMI", "PLATINUMII", "PLATINUMIII", "PLATINUMIV",
                "DIAMONDI", "DIAMONDII", "DIAMONDIII", "DIAMONDIV"};

        for(int i=0; i<tierArr.length; i++){
            if(tierArr[i].equals(tier)){
                return i;
            }
        }

        return -1;
    }

    @Override
    public List<MatchingDTO> getMatchedUserInfo() {
        System.out.println("#@#@$%#@%#@%@# "+matchDAO.getMatchingUsers().get(0).getClass().getName());
        return matchDAO.getMatchingUsers();
    }
}
