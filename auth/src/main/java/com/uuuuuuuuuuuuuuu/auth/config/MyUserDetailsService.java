
package com.uuuuuuuuuuuuuuu.auth.config;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.uuuuuuuuuuuuuuu.core.service.UserAccountService;
import com.uuuuuuuuuuuuuuu.core.service.UserPassportService;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.model.entity.UserAccount;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


//@Component
@Primary
@Service(value = "userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserPassportService userPassportService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPassportService.getUserByPassport(username,1);
        if (user==null) {
            throw new RuntimeException("用户名[" + username + "]账号不存在！");
        }
        UserDto userDto = new UserDto();
        userDto.setPkId(user.getId());
        userDto.setUsername(username);
        userDto.setIsEnabled(true);
        userDto.setPassword("$2a$10$MuipaCvr75sFnnIes6gF5OqRgZx8rD5evalnakyWqOVAZXtWUmgoW");
        /*userDto.setMobile(account.getCurrentMdtskMobNumber());
        userDto.setEmail(account.getCurrentMdtskEmailNumber());
        userDto.setPassword(account.getCurrentMdtskCipherCode());
        userDto.setIsEnabled(account.getStatus()==0);*/
        // 用户拥有的权限
        userDto.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return userDto;
    }
    @DS("slave")
    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        User user = userPassportService.getUserByPassport(phone,1);
        if (user==null) {
            throw new RuntimeException("手机号[" + phone + "]账号不存在！");
        }
        UserDto userDto = new UserDto();
        /*userDto.setPkId(account.getPkId());
        userDto.setMobile(phone);
        userDto.setUsername(account.getCurrentMdtskCustomNumber());
        userDto.setEmail(account.getCurrentMdtskEmailNumber());
        userDto.setPassword(account.getCurrentMdtskCipherCode());
        userDto.setIsEnabled(account.getStatus()==0);*/
        // 用户拥有的权限
        userDto.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return userDto;
    }
    @DS("slave")
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userPassportService.getUserByPassport(email,1);
        if (user==null) {
            throw new RuntimeException("邮箱[" + email + "]账号不存在！");
        }
        UserDto userDto = new UserDto();
      /*  userDto.setPkId(account.getPkId());
        userDto.setEmail(email);
        userDto.setUsername(account.getCurrentMdtskCustomNumber());
        userDto.setMobile(account.getCurrentMdtskMobNumber());
        userDto.setPassword(account.getCurrentMdtskCipherCode());
        userDto.setIsEnabled(account.getStatus()==0);*/
        // 用户拥有的权限
        userDto.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return userDto;
    }
}

