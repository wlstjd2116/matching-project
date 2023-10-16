package coop.jstp.tp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.vo.MatchStartDTO;
import coop.jstp.tp.vo.MatchedUsersDTO;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.TestDTO;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MatchingService {

    public ResponseEntity getUserInfo(String userName) throws JsonProcessingException;

    // 매칭 ON
    public void matchingStart(MatchStartDTO dto);

    // 매칭 off
    public void matchingEnd(int userNum);

    // 매칭 중인 사람이 있는지 확인
    public int matchingOther();

    public List<MatchingDTO> matchedUser();

    public TestDTO getSummonerName(int uNum);

    // 티어를 숫자로 매칭
    public int tierList(String tier);

    // 매칭티어 정보 가져오기
    public List<HashMap<?, ?>> getMatchedUserInfo();

    // 매칭된 유저 테이블에 저장
    public void saveUserList(MatchedUsersDTO dto);

    public List<MatchedUsersDTO> callSavedUserList();

    public List<?> matching(MatchStartDTO dto);

    public void matchingComplete(int matchingNumber);

    public void setSummonerNames(List<?> summonerNames);

    public List<?> getSummonerNames();
}
