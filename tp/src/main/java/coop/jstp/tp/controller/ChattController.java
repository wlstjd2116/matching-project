package coop.jstp.tp.controller;

import coop.jstp.tp.vo.SocketVO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ChattController {
    @MessageMapping("/receive")
    @SendTo("/send")
    public SocketVO SocketHandler(SocketVO socketVO){

        //VO에서 값 가져와서
        String userName = socketVO.getUserName();
        String msg = socketVO.getMsg();
        // 생성자로 반환 값 생성
        SocketVO result = new SocketVO(userName, msg);

        return result;
    }

}
