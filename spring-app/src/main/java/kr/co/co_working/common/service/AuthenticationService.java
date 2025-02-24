package kr.co.co_working.common.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.co.co_working.common.dto.AuthenticationRequestDto;
import kr.co.co_working.member.Member;
import kr.co.co_working.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * isValidMember : 사용자 유효여부 확인
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public boolean isValidMember(AuthenticationRequestDto.LOGIN dto) throws NoSuchElementException, Exception {
        boolean isValid = false;

        Optional<Member> optionalMember = memberRepository.findById(dto.getEmail());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
                isValid = true;
                member.updateDelFlag("0");
            }
        } else {
            throw new NoSuchElementException("해당 멤버가 존재하지 않습니다.");
        }

        return isValid;
    }

    /**
     * isValidPassword : 비밀번호 일치여부 확인
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void isValidPassword(AuthenticationRequestDto.CHECK dto) throws NoSuchElementException, Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
                throw new NoSuchElementException("비밀번호가 일치하지 않습니다.");
            }
        }
    }
}
