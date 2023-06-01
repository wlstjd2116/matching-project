package coop.jstp.tp.dao;

import coop.jstp.tp.vo.MatchStartDTO;
import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MatchDAO {



    // 매칭 시작
    public void matchingStart(MatchStartDTO dto);

    // 매칭 중단
    public void matchingEnd(int userNum);

    // 매칭 중인 사람 있는지 check
    public int matchingOther();

    public List<MatchingDTO> matchedUser();

    public TestDTO getSummonerName(int uNum);

    public List<HashMap<?,?>> getMatchingUsers();

}
