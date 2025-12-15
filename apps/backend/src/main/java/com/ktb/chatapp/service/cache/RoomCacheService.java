package com.ktb.chatapp.service.cache;

import com.ktb.chatapp.model.Room;
import com.ktb.chatapp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Room 관련 캐시 서비스.
 *
 * MongoDB 조회를 줄이고 Redis 캐시를 활용하여 성능을 개선한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomCacheService {

    private final RoomRepository roomRepository;

    /**
     * Room 정보 조회 (캐시 적용)
     */
    @Cacheable(value = "rooms", key = "#roomId", unless = "#result == null")
    public Optional<Room> findById(String roomId) {
        log.debug("Cache miss - fetching room from DB: {}", roomId);
        return roomRepository.findById(roomId);
    }

    /**
     * Room 참여자 수 조회 (캐시 적용)
     */
    @Cacheable(value = "roomParticipantCount", key = "#roomId")
    public int countParticipants(String roomId) {
        log.debug("Cache miss - counting participants from DB: {}", roomId);
        return roomRepository.countParticipants(roomId);
    }

    /**
     * Room 캐시 무효화 (참여자 변경 시 호출)
     */
    @CacheEvict(value = {"rooms", "roomParticipantCount"}, key = "#roomId")
    public void evictRoomCache(String roomId) {
        log.debug("Evicting room cache: {}", roomId);
    }

    /**
     * 참여자 추가 (캐시 무효화 포함)
     */
    @CacheEvict(value = {"rooms", "roomParticipantCount"}, key = "#roomId")
    public void addParticipant(String roomId, String userId) {
        roomRepository.addParticipant(roomId, userId);
        log.debug("Added participant {} to room {}, cache evicted", userId, roomId);
    }

    /**
     * 참여자 제거 (캐시 무효화 포함)
     */
    @CacheEvict(value = {"rooms", "roomParticipantCount"}, key = "#roomId")
    public void removeParticipant(String roomId, String userId) {
        roomRepository.removeParticipant(roomId, userId);
        log.debug("Removed participant {} from room {}, cache evicted", userId, roomId);
    }
}
