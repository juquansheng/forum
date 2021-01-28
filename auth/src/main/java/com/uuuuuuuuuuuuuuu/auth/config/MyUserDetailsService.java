
package com.uuuuuuuuuuuuuuu.auth.config;


import com.uuuuuuuuuuuuuuu.core.service.UserPassportService;
import com.uuuuuuuuuuuuuuu.model.constant.PassPortConst;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//@Component
@Primary
@Service(value = "userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserPassportService userPassportService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPassportService.getUserByPassport(username, PassPortConst.LOGIN_ACCOUNT);
        if (user==null) {
            throw new RuntimeException("用户名[" + username + "]账号不存在！");
        }
        return userToUserDto(user);
    }

    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        User user = userPassportService.getUserByPassport(phone,PassPortConst.LOGIN_PHONE);
        if (user==null) {
            throw new RuntimeException("手机号[" + phone + "]账号不存在！");
        }
        return userToUserDto(user);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userPassportService.getUserByPassport(email,PassPortConst.LOGIN_EMAIL);
        if (user==null) {
            throw new RuntimeException("邮箱[" + email + "]账号不存在！");
        }
        return userToUserDto(user);
    }

    private UserDto userToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setPkId(user.getId());
        userDto.setHash(user.getHash());
        userDto.setUsername(user.getUserName());
        userDto.setPassword(user.getPassword());
        userDto.setIsEnabled(user.getStatus()==0);
        // 用户拥有的权限
        userDto.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return userDto;
    }
}

