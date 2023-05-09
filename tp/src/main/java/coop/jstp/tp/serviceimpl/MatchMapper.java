package coop.jstp.tp.serviceimpl;

import coop.jstp.tp.vo.TestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchMapper {

    // 매칭 시작
    public void matchingStart(int userNum);

    // 매칭 중단
    public void matchingEnd(int userNum);

    // 매칭 중인 사람 있는지 check
    public int matchingOther();
}
