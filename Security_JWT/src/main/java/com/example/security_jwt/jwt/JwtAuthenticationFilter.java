package com.example.security_jwt.jwt;

import com.example.security_jwt.security.UserPrincipleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserPrincipleService userPrincipleService;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        //Check Header có chứa tt jwt k
        //Check bearerToken có chứa nội dung văn bản hay không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /*Nó được gọi mỗi khi một yêu cầu được gửi đến một servlet, được thiết kế để đảm bảo rằng một hoạt động
     chỉ được thực hiện một lần cho mỗi yêu cầu, ngay cả khi yêu cầu có thể đi qua bộ lọc này nhiều lần.*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.getUserNameFormJWT(jwt);
                UserDetails userDetails = userPrincipleService.loadUserByUsername(username);
                if (userDetails != null) {
                    /*Hàm tạo này thường được sử dụng để đặt xác thực người dùng vào Security Context Holder
                    của Spring Security. Bằng cách chỉ thêm danh sách quyền đã được cấp vào hàm tạo, bạn đang
                     nói rằng người dùng đã được xác thực thành công, ngay cả khi danh sách đó trống.*/
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                            (userDetails, null, userDetails.getAuthorities());
                    //tao object chứa chi tiết như địa chỉ IP nguồn và ID phiên
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  /* Đặt authenticationToken vào SecurityContext, cho phép nó được truy cập trong suốt
                   thời gian xử lý yêu cầu hiện tại.*/
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception e) {
            log.error("Fail on set user authentication", e);
        }
        /*cho phép HttpRequest đi tới DispatcherServlet của Spring và @RestControllers @Controllers.*/
        filterChain.doFilter(request, response);
    }
}
