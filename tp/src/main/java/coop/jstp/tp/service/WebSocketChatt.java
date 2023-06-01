package coop.jstp.tp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@ServerEndpoint(value="/chatt")
public class WebSocketChatt {

    // 새로운 클라이언트가 접속할 때마다 클라이언트의 세션 관련 정보를 정적형으로 저장하여 1:N의 통신이 가능하도록 해야함.
    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session s) throws Exception{
        log.info("Open Session : "+ s.toString());
        if(!clients.contains(s)){
            clients.add(s);
            log.info(s+ " add complete.");
        }else{
            log.info("already opened session !");
        }
    }


    @OnMessage // 메시지가 수신되었을 때
    public void onMessage(String msg, Session s) throws Exception{
        log.info("receive message : "+ msg);
        for(Session session: clients){
            log.info("send Data : "+msg );
            s.getBasicRemote().sendText(msg);
        }
    }

    @OnClose // 클라이언트가 브라우저를 종료하거나 다른 페이지로 이동하였을 때
    public void onClose(Session s) throws Exception{
        log.info(s +", session close.");
        clients.remove(s);
    }

}
