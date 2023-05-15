package coop.jstp.tp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public interface MatchingService {

    public ResponseEntity getUserInfo(String userName) throws JsonProcessingException;

    // 매칭 ON
    public void matchingStart(int userNum);

    // 매칭 off
    public void matchingEnd(int userNum);

    // 매칭 중인 사람이 있는지 확인
    public int matchingOther();

    public List<MatchingDTO> matchedUser();

    public TestDTO getSummonerName(int uNum);

}
