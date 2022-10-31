package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.LikeService;
import by.maksimruksha.mapgeneration.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/{mapId}")
    public ResponseEntity<Long> getMapLikes(@PathVariable Long mapId)
    {
        return ResponseEntity.ok(likeService.countAllByMap(mapId));
    }

    @PostMapping("/add")
    public ResponseEntity<LikeDto> addLike(@RequestBody LikeDto likeDto)
    {
        return ResponseEntity.ok(likeService.create(likeDto));
    }

    @GetMapping("/userLikeExists")
    public ResponseEntity<Boolean> userLikeExists(Long userId, Long mapId)
    {
        return ResponseEntity.ok(likeService.userLikeExists(userId, mapId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteLike(Long userId, Long mapId)
    {
        return ResponseEntity.ok(likeService.delete(userId, mapId));
    }
}
