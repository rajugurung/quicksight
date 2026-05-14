package com.quicksight.quicksight_client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.quicksight.QuickSightClient;
import software.amazon.awssdk.services.quicksight.model.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class QuicksightClient {
    private final String accountId = "846465295034";
    private final String seoDashboardArn = "arn:aws:quicksight:us-east-1:846465295034:dashboard/30f75183-c4f3-4451-a183-944f4f63dfa1";
    private final String orderByDayDashboardArn = "arn:aws:quicksight:us-east-1:846465295034:dashboard/2a0f2d42-7cf4-491e-b5fb-efbc7d5eb682";
    @Value("${instance:devf}")
    private String env;
    private final QuickSightClient quickSightClient;

    private String devfSharedFolderId = "ee9ee40a-fd53-4ff5-a7b6-0ee0b5cfe5e7";

    public String generateEmbedUrl(DashboardSummary dashboard, String email) {
        if( email == null || email.isBlank()) {
//            return generateEmbedUrlForAnonymousUser(dashboard);
            return generateEmbedUrlForWithRls(dashboard);
        }
        return generateEmbedUrlForRegisteredUser(email, dashboard);
    }

    public String generateEmbedUrlForWithRls(DashboardSummary dashboard) {
        QuickSightClient quickSightClient = QuickSightClient.builder()
                .region(Region.US_EAST_1) // Or your QuickSight region
                .build();

        List<SessionTag> sessionTags = Arrays.asList(
                SessionTag.builder().key("orderId").value("1,4").build()
//                SessionTag.builder().key("orderId2").value("2").build()
        );

        // Configure the embedding request
        GenerateEmbedUrlForAnonymousUserRequest request = GenerateEmbedUrlForAnonymousUserRequest.builder()
                .awsAccountId(accountId)
                .namespace("default") // Or your custom namespace
                .sessionLifetimeInMinutes(60L) // Optional: adjust session duration
                .authorizedResourceArns(Arrays.asList(dashboard.getArn()))
                .sessionTags(sessionTags)
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



    public String generateEmbedUrlForAnonymousUser(String dashboard) {
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
                .authorizedResourceArns(Arrays.asList(dashboardUrlToRetrieve, orderByDayDashboardArn))
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

    public boolean isRegisteredUser(String email) {
        String namespace = "default"; // Replace if you're using a different namespace

        DescribeUserRequest describeUserRequest = DescribeUserRequest.builder()
                .awsAccountId(accountId)
                .userName(email)
                .namespace(namespace)
                .build();
            try (QuickSightClient quickSightClient = QuickSightClient.builder()
                    .region(Region.US_EAST_1) // Or your QuickSight region
                    .build()) {
                try {
                    DescribeUserResponse response = quickSightClient.describeUser(describeUserRequest);
                    return true;
                } catch (ResourceNotFoundException e) {
                    System.out.println("User " + email + " is NOT a registered QuickSight user.");
                    return false;
                } catch (QuickSightException e ) {
                    System.err.println("Error describing QuickSight user: " + e.getMessage());
                    // Handle other QuickSight API errors
                }
//arn:aws:quicksight:us-east-1:846465295034:namespace/default
            }
            return false;
    }

    public String generateEmbedUrlForAnonymousUser(DashboardSummary dashboard) {
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

    public String generateEmbedUrlForAnonymousUserWithParam(DashboardSummary dashboard) {
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

//    public String generateEmbedUrlForRegisteredUser(String userId, DashboardSummary dashboard) {
//        QuickSightClient quickSightClient = QuickSightClient.builder()
//                .region(Region.US_EAST_1)
//                .build();
//        String userArnTemplate = "arn:aws:quicksight:YOUR_REGION:YOUR_AWS_ACCOUNT_ID:user/default/YOUR_QUICKSIGHT_USERNAME"; // Replace with your user ARN
//        String userArn = userArnTemplate.replace("YOUR_REGION", Region.US_EAST_1.id()).replace("YOUR_AWS_ACCOUNT_ID", accountId).replace("YOUR_QUICKSIGHT_USERNAME", userId);
//
//        RegisteredUserEmbeddingExperienceConfiguration experienceConfig = RegisteredUserEmbeddingExperienceConfiguration.builder()
//                    .dashboard(dashboardConfig)
//                    .build();
//        // Create the GetDashboardEmbedUrlRequest
//        GetDashboardEmbedUrlRequest request = GetDashboardEmbedUrlRequest.builder()
//                .awsAccountId(accountId)
//                .dashboardId(dashboard.getDashboardId())
//                .userArn(userArn)
//                .identityType(IdentityType.QUICKSIGHT.name()) // Use QUICKSIGHT for registered users
//                .sessionLifetimeInMinutes(600L) // Optional: Set the session lifetime in minutes
//                .build();
//
//        String embedUrl = "";
//        try {
//            // Get the embed URL
//            GetDashboardEmbedUrlResponse response = quickSightClient.getDashboardEmbedUrl(request);
//             embedUrl= response.embedUrl();
//
//            System.out.println("Generated QuickSight Embed URL: " + embedUrl);
//
//        } catch (Exception e) {
//            System.err.println("Error generating QuickSight embed URL: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            quickSightClient.close();
//        }
//        return embedUrl;
//    }

    public String generateEmbedUrlForRegisteredUser(String userId, DashboardSummary dashboard) {
        String userArnTemplate = "arn:aws:quicksight:YOUR_REGION:YOUR_AWS_ACCOUNT_ID:user/default/YOUR_QUICKSIGHT_USERNAME"; // Replace with your user ARN
        String userArn = userArnTemplate.replace("YOUR_REGION", Region.US_EAST_1.id()).replace("YOUR_AWS_ACCOUNT_ID", accountId).replace("YOUR_QUICKSIGHT_USERNAME", userId);
        QuickSightClient quickSightClient = QuickSightClient.builder()
                .region(Region.US_EAST_1)
                .build();
        GenerateEmbedUrlForRegisteredUserResponse response = null;

        try {
            RegisteredUserDashboardEmbeddingConfiguration dashboardConfig = RegisteredUserDashboardEmbeddingConfiguration.builder()
                    .initialDashboardId(dashboard.getDashboardId())
                    .build();

            RegisteredUserEmbeddingExperienceConfiguration experienceConfig = RegisteredUserEmbeddingExperienceConfiguration.builder()
                    .dashboard(dashboardConfig)
                    .build();

            GenerateEmbedUrlForRegisteredUserRequest request = GenerateEmbedUrlForRegisteredUserRequest.builder()
                    .awsAccountId(accountId)
                    .userArn(userArn)
                    .sessionLifetimeInMinutes(60L) // Adjust session lifetime as needed (15 mins to 10 hours)
                    .experienceConfiguration(experienceConfig)
                    .build();

            response = quickSightClient.generateEmbedUrlForRegisteredUser(request);

            String embedUrl = response.embedUrl();
            System.out.println("Generated Embed URL: " + embedUrl);


        } catch (Exception e) {
            System.err.println("Error generating embed URL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            quickSightClient.close();
        }
        return response.embedUrl();
    }

    public ListDashboardsResponse getListOfDashboards() {
//        QuickSightClient quickSightClient = QuickSightClient.builder()
//                .region(Region.US_EAST_1)
//                .build();
        ListDashboardsRequest listDashboardsRequest = ListDashboardsRequest.builder()
                .awsAccountId(accountId).build();
        ListDashboardsResponse listDashboardsResponse = quickSightClient.listDashboards(listDashboardsRequest);
        return listDashboardsResponse;
    }

    public List<String> getDashboardArns() {
        QuickSightClient quickSightClient = QuickSightClient.builder()
                .region(Region.US_EAST_1)
                .build();

        ListFolderMembersRequest listRequest = ListFolderMembersRequest.builder().awsAccountId(accountId).folderId(devfSharedFolderId).build();
        ListFolderMembersResponse listResponse = quickSightClient.listFolderMembers(listRequest);
        List<String> dashboardArns = listResponse.folderMemberList().stream().map(MemberIdArnPair::memberArn).filter(s -> s
                .contains(":dashboard/")).toList();
        return dashboardArns;
    }

    private String extractDashboardIdFromArn(String dashboardArn) {
        // Example ARN format: "arn:aws:quicksight:us-east-1:123456789012:dashboard/a1b2c3d4-5678-90ab-cdef-EXAMPLE11111"
        return dashboardArn.substring(dashboardArn.lastIndexOf('/') + 1);
    }



}
