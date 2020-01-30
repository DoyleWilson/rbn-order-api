package com.rcn.order.common;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * The MdcFilter program that is used for logging correlation id in the logger.
 *
 * @author GKC-Dev-AU-Team
 * @version 1.0
 * @since 2019-11-22
 */

@WebFilter
@Component
public class MdcFilter extends HttpFilter {

    /**
     * This method is used for creating files in S3
     * @param request request
     * @param response response details
     * @param filterChain filter details
     **/
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            MDC.put("CorrelationId", getCorrelationId());
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("CorrelationId");
        }
    }
    private String getCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
