package com.blubbax.esa.transactionManager.workload;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WorkloadController {

    WorkloadService workloadService;

    public WorkloadController(WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @Operation(summary = "Set current readiness status")
    @GetMapping("/api/workload/{number}")
    public void setApplicationReadiness(@PathVariable long number) {
        this.workloadService.doWork(number);
    }

}
