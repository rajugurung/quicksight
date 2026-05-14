package com.quicksight.quicksight_client;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class QuicksightController {
    private final QuicksightClient quicksightClient;

    @GetMapping(path = "/embedurl")
    public QuicksightUrlResponse getEmbedRul(@RequestParam(name = "dashboard", required = false) String dashboard) {
        return new QuicksightUrlResponse(quicksightClient.generateEmbedUrlForAnonymousUser(dashboard));
    }

    @GetMapping(path = "/dashboards")
    public ResponseEntity<List<DashboardSummary>> getListOfDashboards() {
        List<DashboardSummary> dashboardSummaries = new ArrayList<>();
        quicksightClient.getListOfDashboards().dashboardSummaryList().forEach(
                dashboardSummary -> dashboardSummaries.add(DashboardSummary.builder()
                                .arn(dashboardSummary.arn())
                                .name(dashboardSummary.name())
                                .dashboardId(dashboardSummary.dashboardId())
                        .build())
        );
        return new ResponseEntity<>(dashboardSummaries, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboards/arns")
    public ResponseEntity<List<String>> getDashboardsArns() {
        List<DashboardSummary> dashboardSummaries = new ArrayList<>();
        return new ResponseEntity<>(quicksightClient.getDashboardArns(), HttpStatus.OK);
    }

    @GetMapping(path = "/dashboards/{dashboardId}")
    public QuicksightUrlResponse getEmbedRul2(@PathVariable String dashboardId, @RequestParam String arn, @RequestParam(required = false) String email) {
        return new QuicksightUrlResponse(quicksightClient.generateEmbedUrl(
                DashboardSummary.builder().arn(arn).dashboardId(dashboardId).build(), email));
    }

    @GetMapping(path = "/isRegistered")
    public boolean isRegisteredQuicksightUser( @RequestParam String email) {
        return quicksightClient.isRegisteredUser(email);
    }
}
