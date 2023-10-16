package coop.jstp.tp.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.dao.MatchDAO;
import coop.jstp.tp.service.MatchingService;
import coop.jstp.tp.vo.MatchStartDTO;
import coop.jstp.tp.vo.MatchedUsersDTO;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.TestDTO;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("service")
public class MatchingServiceImpl implements MatchingService {

    @Value("${riot.apikey}")
    private String apiKey;

    @Autowired
    private MatchDAO matchDAO;
    private List<?> summonerNames;

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
        log.info("매칭 중단된 사람 : "+userNum);
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
    public List<HashMap<?, ?>> getMatchedUserInfo() {
        return matchDAO.getMatchingUsers();
    }

    @Override
    public void saveUserList(MatchedUsersDTO dto) {
        matchDAO.saveUserList(dto);
    }

    @Override
    public List<MatchedUsersDTO> callSavedUserList() {
        return matchDAO.callSavedUserList();
    }

    @Override
    public List<?> matching(MatchStartDTO mDto) {
        List<HashMap<?, ?>> list = getMatchedUserInfo();
        log.info(list.toString());
        log.info(mDto.toString());
        var matchflag = false;
        // 반환될 유저 names
        List<String> summonerNames = new ArrayList<>();

        for(int i=0; i<list.size(); i++){
            if((int)list.get(i).get("MATCHING_TIER") == mDto.getMatchingTier()){
                log.info("매칭 중인 다른 사용자가 있습니다.");
                List<MatchingDTO> summonerList = matchedUser();

                //매칭된 두 사람을 매칭 DB에서 제외
                for (MatchingDTO matchingDTO : summonerList) {
                    TestDTO summonerName = getSummonerName(matchingDTO.getU_NUM());
                    TestDTO summonerName2 = getSummonerName((mDto.getUserNum()));

                    // front로 반환 될 List
                    summonerNames.add(summonerName.getSummoner_Name());
                    summonerNames.add(summonerName2.getSummoner_Name());
                    matchingEnd(matchingDTO.getU_NUM());
                }
                log.info("매칭 완료되었습니다.");

                // dto 에 인자 값을 지정 후 생성자 생성
                MatchedUsersDTO dto = new MatchedUsersDTO();
                dto.setUser2(summonerList.get(0).getU_NUM());
                dto.setUser1(mDto.getUserNum());

                // 매칭된 유저를 DB에 저장
                saveUserList(dto);

                // 매칭 DB에 올려진 user 삭제
                matchingEnd(summonerList.get(0).getU_NUM());
                matchingEnd(mDto.getUserNum());

                matchflag = true;
            }
        }

        if(!matchflag){
            // 매칭 등록
            log.info("Matching start user : "+ mDto.getUserNum());
            matchingStart(mDto);
        }

        return summonerNames;
    }

    @Override
    public void matchingComplete(int matchingNumber) {
        matchDAO.matchingComplete(matchingNumber);
    }

    @Override
    public void setSummonerNames(List<?> summonerNames) {
        this.summonerNames = summonerNames;
    }

    @Override
    public List<?> getSummonerNames() {
        return this.summonerNames;
    }
}
