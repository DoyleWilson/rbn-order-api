package com.rcn.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * The model DesJobRequest for job details
 *
 * @author GKC-Dev-AU-Team
 * @version 1.0
 * @since 2019-11-22
 */
@Getter
@Setter
@ToString
@Component
public class DesJobRequest {

    private String Order;
    private String fileName;
    private String fileContent;
    private String transactionId;
}
