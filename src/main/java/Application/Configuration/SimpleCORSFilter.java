package Application.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@Component
@Slf4j
public class SimpleCORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initilisation du Middleware");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        log.info("start");

        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            log.info("request header " + requestHeaderNames.nextElement());
        }

        List<String> responseHeaderNames = new ArrayList<>(((HttpServletResponse) servletResponse).getHeaderNames());
        for (String responseHeaderName : responseHeaderNames) {
            log.info("response header " + responseHeaderName);
        }

        log.info("stop");

        //((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "https://verymagicduck.netlify.app");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers","Authorization");

        filterChain.doFilter(request, servletResponse);

        //responseToUse.setHeader("Access-Control-Allow-Origin", requestToUse.getHeader("Origin"));
        //responseToUse.setHeader("Authorization", "*");
        //filterChain.doFilter(requestToUse,responseToUse);
    }

    @Override
    public void destroy() {

    }
}