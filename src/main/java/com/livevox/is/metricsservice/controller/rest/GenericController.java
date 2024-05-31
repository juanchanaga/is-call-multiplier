package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.metricsservice.domain.*;
import com.livevox.is.metricsservice.service.GenericService;
import jakarta.servlet.http.HttpServletResponse;
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
    public GenericResponse<Agent> getAgents(@RequestBody AgentRequest req,
                                            HttpServletResponse httpResp) {
        if(req != null){
            return genericService.getAgents(req);
        } else {
            return null;
        }
    }

    @GetMapping(value = "/callCentersAndSkills/{appName}/{token}/")
    public GenericResponse<Client> getDashBoards(@PathVariable String appName, @PathVariable String token,
                                                 HttpServletResponse httpResp) {

        GenericResponse<Client> resp;
        resp = genericService.getDashBoards(appName, token);

        return resp;
    }
}
