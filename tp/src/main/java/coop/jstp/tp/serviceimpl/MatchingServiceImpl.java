package coop.jstp.tp.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.vo.SummonerDTO;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service("service")
public class MatchingServiceImpl implements MatchingService {

    @Autowired
    private MatchMapper matchMapper;

    @Override
    public String testService() {
        RestTemplate rt = new RestTemplate();

        // request resource
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<?> requestMessage = new HttpEntity<>(body, headers);

        //URI
        String api_key = "RGAPI-bdbca488-9ffb-498e-bd36-830af50b1ea8";
        String userName = "엘릭시";
        String url ="https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + userName + "?api_key=" + api_key;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        HttpEntity<String> response = rt.exchange(uri.toString(), HttpMethod.GET, requestMessage, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        System.out.println(response);
        return null;
    }

    @Override
    public ResponseEntity getUserInfo(String userName) throws JsonProcessingException {

        // 통신에 필요한 라이브러리, 타입, JSON 등 선언
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> requestMessage = new HttpEntity<>(body, headers);
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray jsonArray = new JSONArray();
        // URI builder
        String api_key = "RGAPI-e0d8fbf6-ef95-4ba5-ab3f-80a8ae6ebaaf"; // 외부 노출 금지
        String url ="https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + userName + "?api_key=" + api_key;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        //ResponseEntity Extends HttpEntity
        // uri Request
        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestMessage, String.class);

        //Dto -> JSON 객체로 변환
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        SummonerDTO sDto = objectMapper.readValue(response.getBody(), SummonerDTO.class);

//        Map<String, Object> data = new HashMap<>();
//        data.put("data", sDto);
//        String json="";
//        try{
//            json = new ObjectMapper().writeValueAsString(data);
//        }catch (Exception e){
//            e.getMessage();
//        }
//        org.json.JSONObject jObj = new org.json.JSONObject(json);

        return response;
    }

    // 매칭 시작
    @Override
    public void matchingStart(int userNum) {
        matchMapper.matchingStart(userNum);
    }

    // 매칭 중단
    @Override
    public void matchingEnd(int userNum) {
        matchMapper.matchingEnd(userNum);
    }
}
