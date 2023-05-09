package coop.jstp.tp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface MatchingService {
    public String testService();
    public ResponseEntity getUserInfo(String userName) throws JsonProcessingException;

    // 매칭 ON
    public void matchingStart(int userNum);

    // 매칭 off
    public void matchingEnd(int userNum);
}
