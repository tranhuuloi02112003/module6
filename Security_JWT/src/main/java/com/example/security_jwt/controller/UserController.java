package com.example.security_jwt.controller;


import com.example.security_jwt.entity.Users;
import com.example.security_jwt.jwt.JwtTokenProvider;
import com.example.security_jwt.security.UserPrinciple;
import com.example.security_jwt.service.IUserService;
import com.example.security_jwt.test.JwtDTO;
import com.example.security_jwt.test.PasswordChangeDTO;
import com.example.security_jwt.test.SignInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDTO.getUserName(), signInDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Lưu trữ thông tin xác thực SecurityContext.
        //Khi một yêu cầu đến, bộ lọc sẽ kiểm tra SecurityContext để xem người dùng đã được xác thực hay chưa.
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String token = jwtTokenProvider.genarateToken(userPrinciple);
        return ResponseEntity.
                ok(new JwtDTO(token, userPrinciple.getUsername(), userPrinciple.getAuthorities()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = iUserService.findByUserName(userPrinciple.getUsername());
        if (passwordEncoder.matches(passwordChangeDTO.getOldPassword(), users.getPassword())) {
            if (passwordChangeDTO.getConfimPassword().equalsIgnoreCase(passwordChangeDTO.getNewPassword())) {
                String encodedPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
                users.setPassword(encodedPassword);
                iUserService.changePassword(users);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Mật khẩu mới và mật khẩu xác nhận không trùng khớp", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Mật khẩu cũ không đúng", HttpStatus.BAD_REQUEST);
        }
    }
}













