package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.metricsservice.domain.*;
import com.livevox.is.metricsservice.service.GenericService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
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
    public ResponseEntity<GenericResponse<Agent>> getAgents(@Valid @RequestBody AgentRequest req) {

        GenericResponse<Agent> response = new GenericResponse<>();

        if(req.getCount() == null){
            req.setCount(1000);
        }

        if(req.getCount() == null){
            req.setCount(0);
        }

        response.setData(genericService.getAgents(req));

        if(response.getData().isEmpty()){
            log.info("No found agents");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/callCentersAndSkills/{appName}/{token}/")
    public GenericResponse<Client> getDashBoards(@PathVariable String appName, @PathVariable String token,
                                                 HttpServletResponse httpResp) {

        GenericResponse<Client> resp;
        resp = genericService.getDashBoards(appName, token);

        return resp;
    }
}
