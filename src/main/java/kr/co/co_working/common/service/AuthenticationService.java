package kr.co.co_working.common.service;

import kr.co.co_working.common.dto.AuthenticationRequestDto;
import kr.co.co_working.member.Member;
import kr.co.co_working.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isValidMember(AuthenticationRequestDto.LOGIN dto) throws NoSuchElementException, Exception {
        boolean isValid = false;

        Optional<Member> optionalMember = memberRepository.findById(dto.getEmail());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
                isValid = true;
            }
        } else {
            throw new NoSuchElementException("해당 멤버가 존재하지 않습니다.");
        }

        return isValid;
    }
}
