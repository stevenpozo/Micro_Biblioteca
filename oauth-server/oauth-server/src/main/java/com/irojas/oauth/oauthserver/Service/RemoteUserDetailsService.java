package com.irojas.oauth.oauthserver.Service;

import com.irojas.oauth.oauthserver.Client.UserClient;
import com.irojas.oauth.oauthserver.Modals.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RemoteUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
        UserDTO userDTO = userClient.getUserByCode(code);

        if (userDTO == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el mail: " + code);
        }

        // Mapear el rol recibido (por ejemplo, "ADMIN" o "USER") a una GrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userDTO.getRole());

        return new User(userDTO.getCode(), userDTO.getPassword(), Collections.singletonList(authority));
    }
}
