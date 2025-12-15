package com.ktb.chatapp.service.cache;

import com.ktb.chatapp.model.User;
import com.ktb.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User 관련 캐시 서비스.
 *
 * 사용자 정보는 자주 변경되지 않으므로 30분 TTL로 캐싱한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final UserRepository userRepository;

    /**
     * User ID로 조회 (캐시 적용)
     */
    @Cacheable(value = "users", key = "#userId", unless = "#result == null")
    public Optional<User> findById(String userId) {
        log.debug("Cache miss - fetching user by id from DB: {}", userId);
        return userRepository.findById(userId);
    }

    /**
     * User Email로 조회 (캐시 적용)
     */
    @Cacheable(value = "users", key = "'email:' + #email", unless = "#result == null")
    public Optional<User> findByEmail(String email) {
        log.debug("Cache miss - fetching user by email from DB: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * User 캐시 무효화 (프로필 업데이트 시 호출)
     */
    @CacheEvict(value = "users", allEntries = true)
    public void evictAllUserCache() {
        log.debug("Evicting all user cache");
    }

    /**
     * 특정 User 캐시 무효화
     */
    @CacheEvict(value = "users", key = "#userId")
    public void evictUserCache(String userId) {
        log.debug("Evicting user cache: {}", userId);
    }
}
