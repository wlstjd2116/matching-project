package coop.jstp.tp.serviceimpl;

import coop.jstp.tp.vo.MatchingDTO;
import coop.jstp.tp.vo.SummonerDTO;
import coop.jstp.tp.vo.TestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Mapper
public interface MatchMapper {

    // 매칭 시작
    public void matchingStart(int userNum);

    // 매칭 중단
    public void matchingEnd(int userNum);

    // 매칭 중인 사람 있는지 check
    public int matchingOther();

    public ArrayList<MatchingDTO> getMatchedUser();


}
