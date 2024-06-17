package iess.pt.controller;

import iess.pt.entity.UpdateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Async
public class WebSocketController {


    @MessageMapping("/hello")
    @SendTo("/topic/greet")
    public UpdateMessage greeting() throws Exception {
        System.out.println("===============================\nhello here\n===============================");
        return new UpdateMessage("Hello, bitch");
    }
}

