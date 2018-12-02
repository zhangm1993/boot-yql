//package com.ideal.manage.dsp.config;
//
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.UUID;
//
//import static java.lang.Thread.sleep;
//
//@Component
//public class AsyncHelper {
//    @Async
//    public void sse(SseEmitter emitter, long eventNumber, long intervalSec) throws Exception{
//        int i = 0;
//        while(true){
//            i++;
//            sleep(intervalSec);
//            emitter.send(SseEmitter.event().comment("This is test event").id(UUID.randomUUID().toString())
//                    .name("onlog").reconnectTime(3000).data("message"+i));
//            if(i >= 20){
//                break;
//            }
//        }
//
//
//        emitter.complete();
//    }
//}
