package com.quicksight.quicksight_client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.quicksight.model.ListDashboardsResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class QuicksightController {
    private final QuicksightClient quicksightClient;

    @GetMapping(path = "/embedurl")
    public QuicksightUrlResponse getEmbedRul(@RequestParam(name = "dashboard", required = false) String dashboard) {
        return new QuicksightUrlResponse(quicksightClient.generateAnonymousEmbedUrl(dashboard));
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

    @GetMapping(path = "/dashboards/{dashboardId}")
    public QuicksightUrlResponse getEmbedRul2(@PathVariable String dashboardId, @RequestParam String arn) {
        return new QuicksightUrlResponse(quicksightClient.generateAnonymousEmbedUrl(DashboardSummary.builder()
                .arn(arn).dashboardId(dashboardId).build()));
    }
}
