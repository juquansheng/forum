/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.uuuuuuuuuuuuuuu.auth.config.account;


import com.uuuuuuuuuuuuuuu.auth.config.MyUserDetailsService;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * @author
 * @date
 */
@Configuration
public class AccountSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private MyUserDetailsService userDetailsService;


	@Autowired
	private RedisClient redisClient;

	@Override
	public void configure(HttpSecurity http) {
		 AccountAuthenticationProvider accountAuthenticationProvider = AccountAuthenticationProvider
				 .builder()
				 .redisClient(redisClient)
				 .userDetailsService(userDetailsService)
				 .build();
		http.authenticationProvider(accountAuthenticationProvider);
	}

}
