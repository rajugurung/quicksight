package com.quicksight.quicksight_client;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DashboardSummary {
    private String arn;
    private String name;
    private String dashboardId;
}
