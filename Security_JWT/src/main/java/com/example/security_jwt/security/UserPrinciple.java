package com.example.security_jwt.security;

import com.example.security_jwt.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//Lấy userdetails map với userdetails trên csdl
@AllArgsConstructor
public class UserPrinciple implements UserDetails {

    private Integer userId;
    private String userName;
    private String password;
    private Collection<? extends GrantedAuthority> roles;

    /*Việc sử dụng GrantedAuthority thay vì String mang lại một số lợi ích:
    -Tính linh hoạt: GrantedAuthority là một interface, do đó bạn có thể tạo các lớp triển khai riêng của mình
    để thêm các thông tin bổ sung nếu cần.
    -Tính an toàn: Khi sử dụng GrantedAuthority, bạn có thể chắc chắn rằng mỗi đối tượng trong collection đều
    biểu diễn một quyền. Trong khi đó, nếu sử dụng String, bạn có thể vô tình thêm vào một chuỗi không phải là quyền.
    -Tính nhất quán: Spring Security sử dụng GrantedAuthority trong nhiều phần của khung như UserDetails và
    Authentication. Việc sử dụng cùng một kiểu giúp mã của bạn trở nên nhất quán hơn.*/
    // SimpleGrantedAuthority đại diện cho String authority

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public static UserPrinciple mapUserToUserPrinciple(Users users){
     /*   GrantedAuthority là một interface trong Spring Security, đại diện cho một quyền hạn
         được cấp cho Authentication*/
        List<GrantedAuthority> authorities = users.getRoles().stream().
                map(role ->new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
       /* SimpleGrantedAuthority là một lớp cơ bản trong Spring Security, nó cung cấp một cách
         đơn giản để tạo ra một GrantedAuthority*/
        return new UserPrinciple(users.getUserId(), users.getUserName(), users.getPassword(), authorities);
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
