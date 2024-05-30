package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.metricsservice.domain.AgentRequest;
import com.livevox.is.metricsservice.domain.AgentsResponse;
import com.livevox.is.metricsservice.domain.DashBoardResponse;
import com.livevox.is.metricsservice.service.GenericService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class GenericController {

//    @GrpcClient("genericService")
//    private GenericServiceGrpc.GenericServiceBlockingStub genericGrpcServiceStub;

    @Autowired
    GenericService genericService;

//    @PostMapping(value = "/agents")
//    public AgentResponse getAgents(@Valid AgentRequest request) {
//        return genericGrpcServiceStub.getAgents(request);
//    }

    @GetMapping(value = "prueba")
    public String getPrueba() {
        return genericService.getResponse();
    }

    @PostMapping(value = "/agents")
    public AgentsResponse getAgents(@Valid AgentRequest request) {
        if(request != null){
            return genericService.getAgents(request);
        } else {
            return null;
        }
    }

    @GetMapping(value = "callCentersAndSkills/Dashboard/{token}/")
    public DashBoardResponse getDashBoards(@PathVariable(name = "token") String token, @RequestParam(name = "_dc") Long dc) {
        if(dc != null && token != null){
            return genericService.getDashBoards(token, dc);
        } else {
            return null;
        }
    }
}
