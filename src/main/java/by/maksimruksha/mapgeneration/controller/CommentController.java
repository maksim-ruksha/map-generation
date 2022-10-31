package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.CommentService;
import by.maksimruksha.mapgeneration.dto.CommentDto;
import by.maksimruksha.mapgeneration.entities.Comment;
import by.maksimruksha.mapgeneration.util.SortHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/getByMap")
    public ResponseEntity<List<CommentDto>> getMapComments(
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam Long mapId)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortHelper.orderDirection(direction), sortField));
        return ResponseEntity.ok(commentService.findAllByMap(pageable, mapId).getContent());
    }

    @GetMapping("/getPagesCount")
    public ResponseEntity<Long> getPagesCount(@RequestParam Long mapId, @RequestParam Long pageSize)
    {
        return ResponseEntity.ok(commentService.getPagesCount(mapId, pageSize));
    }

    @PostMapping("/send")
    public ResponseEntity<CommentDto> sendComment(@RequestBody CommentDto commentDto)
    {
        return ResponseEntity.ok(commentService.send(commentDto));
    }

    @GetMapping("/userCommentExists")
    public ResponseEntity<Boolean> userCommentExists(@RequestParam Long mapId, @RequestParam Long userId)
    {
        return ResponseEntity.ok(commentService.userCommentExists(mapId, userId));
    }

    @GetMapping("/getUserComment")
    public ResponseEntity<CommentDto> getUserComment(@RequestParam Long mapId, @RequestParam Long userId)
    {
        return ResponseEntity.ok(commentService.findUserComment(mapId, userId));
    }
}
