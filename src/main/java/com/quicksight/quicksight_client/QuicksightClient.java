package com.quicksight.quicksight_client;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.quicksight.QuickSightClient;
import software.amazon.awssdk.services.quicksight.model.*;

import java.util.Arrays;
import java.util.Objects;

@Component
public class QuicksightClient {
    private final String accountId = "846465295034";
    private final String seoDashboardArn = "arn:aws:quicksight:us-east-1:846465295034:dashboard/30f75183-c4f3-4451-a183-944f4f63dfa1";
    private final String orderByDayDashboardArn = "arn:aws:quicksight:us-east-1:846465295034:dashboard/2a0f2d42-7cf4-491e-b5fb-efbc7d5eb682";
//    private final String userArn = "";
//    private final String dashboardExperience = "DASHBOARD";
//
//    QuickSightClient quicksightClient = QuickSightClient.builder().region(Region.US_EAST_1).build();

    public String generateAnonymousEmbedUrl(String dashboard) {
        String dashboardUrlToRetrieve = seoDashboardArn;
        if(Objects.equals("order", dashboard)) {
            dashboardUrlToRetrieve = orderByDayDashboardArn;
        }
        QuickSightClient quickSightClient = QuickSightClient.builder()
                .region(Region.US_EAST_1) // Or your QuickSight region
                .build();

        // Configure the embedding request
        GenerateEmbedUrlForAnonymousUserRequest request = GenerateEmbedUrlForAnonymousUserRequest.builder()
                .awsAccountId(accountId)
                .namespace("default") // Or your custom namespace
                .sessionLifetimeInMinutes(60L) // Optional: adjust session duration
                .authorizedResourceArns(Arrays.asList(dashboardUrlToRetrieve))
                .experienceConfiguration(AnonymousUserEmbeddingExperienceConfiguration.builder()
                        .dashboard(AnonymousUserDashboardEmbeddingConfiguration.builder()
                                .initialDashboardId(extractDashboardIdFromArn(dashboardUrlToRetrieve))
                                .build())
                        .build())
                .build();

        try {
            GenerateEmbedUrlForAnonymousUserResponse response = quickSightClient.generateEmbedUrlForAnonymousUser(request);
            return response.embedUrl();
        } catch (Exception e) {
            System.err.println("Error generating anonymous embed URL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String generateAnonymousEmbedUrl(DashboardSummary dashboard) {
        QuickSightClient quickSightClient = QuickSightClient.builder()
                .region(Region.US_EAST_1) // Or your QuickSight region
                .build();

        // Configure the embedding request
        GenerateEmbedUrlForAnonymousUserRequest request = GenerateEmbedUrlForAnonymousUserRequest.builder()
                .awsAccountId(accountId)
                .namespace("default") // Or your custom namespace
                .sessionLifetimeInMinutes(60L) // Optional: adjust session duration
                .authorizedResourceArns(Arrays.asList(dashboard.getArn()))
                .experienceConfiguration(AnonymousUserEmbeddingExperienceConfiguration.builder()
                        .dashboard(AnonymousUserDashboardEmbeddingConfiguration.builder()
                                .initialDashboardId(extractDashboardIdFromArn(dashboard.getDashboardId()))
                                .build())
                        .build())
                .build();

        try {
            GenerateEmbedUrlForAnonymousUserResponse response = quickSightClient.generateEmbedUrlForAnonymousUser(request);
            return response.embedUrl();
        } catch (Exception e) {
            System.err.println("Error generating anonymous embed URL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ListDashboardsResponse getListOfDashboards() {
        QuickSightClient quickSightClient = QuickSightClient.builder()
                .region(Region.US_EAST_1)
                .build();
        ListDashboardsRequest listDashboardsRequest = ListDashboardsRequest.builder().awsAccountId(accountId).build();
        ListDashboardsResponse listDashboardsResponse = quickSightClient.listDashboards(listDashboardsRequest);
        return listDashboardsResponse;
    }

    private String extractDashboardIdFromArn(String dashboardArn) {
        // Example ARN format: "arn:aws:quicksight:us-east-1:123456789012:dashboard/a1b2c3d4-5678-90ab-cdef-EXAMPLE11111"
        return dashboardArn.substring(dashboardArn.lastIndexOf('/') + 1);
    }



}
