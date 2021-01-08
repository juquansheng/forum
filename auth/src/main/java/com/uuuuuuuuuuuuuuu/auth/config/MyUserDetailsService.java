
package com.uuuuuuuuuuuuuuu.auth.config;


import com.uuuuuuuuuuuuuuu.auth.service.UserAccountService;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.model.entity.UserAccount;
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
    private UserAccountService userAccountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userAccountService.getUserByAccount(username);
        if (account==null) {
            throw new RuntimeException("用户名[" + username + "]账号不存在！");
        }
        UserDto userDto = new UserDto();
        userDto.setPkId(account.getPkId());
        userDto.setUsername(username);
        /*userDto.setMobile(account.getCurrentMdtskMobNumber());
        userDto.setEmail(account.getCurrentMdtskEmailNumber());
        userDto.setPassword(account.getCurrentMdtskCipherCode());
        userDto.setIsEnabled(account.getStatus()==0);*/
        // 用户拥有的权限
        userDto.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return userDto;
    }

    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        UserAccount account = userAccountService.getUserByPhone(phone);
        if (account==null) {
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

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        UserAccount account = userAccountService.getUserByEmail(email);
        if (account==null) {
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

