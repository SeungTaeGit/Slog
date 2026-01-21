package com.slog.blog_api.global.auth.service;

import com.slog.blog_api.domain.member.entity.Member;
import com.slog.blog_api.domain.member.entity.Role;
import com.slog.blog_api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        log.info("📢 구글 로그인 시도: email={}, name={}", email, name);

        Role role = (email != null && email.equals(adminEmail)) ? Role.ROLE_ADMIN : Role.ROLE_USER;

        Member member = saveOrUpdate(email, name, role);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                attributes,
                "sub"
        );
    }

    private Member saveOrUpdate(String email, String name, Role role) {
        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.update(name, role))
                .orElse(Member.builder()
                        .email(email)
                        .name(name)
                        .role(role)
                        .build());

        return memberRepository.save(member);
    }
}
