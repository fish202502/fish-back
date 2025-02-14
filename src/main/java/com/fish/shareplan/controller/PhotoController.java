package com.fish.shareplan.controller;

import com.fish.shareplan.domain.expense.dto.request.ExpenseRequestDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseCreateResponseDto;
import com.fish.shareplan.domain.photo.dto.respnse.ImageUrlResponseDto;
import com.fish.shareplan.domain.photo.entity.ImageUrl;
import com.fish.shareplan.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/fish/photo")
@RestController
public class PhotoController {

    private final PhotoService photoService;

    // 사진 등록
    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<List<ImageUrlResponseDto>> addPhoto(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        List<ImageUrlResponseDto> dtoList = photoService.addPhoto(roomCode, url, images);

        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<List<ImageUrlResponseDto>> getPhoto(
            @PathVariable String roomCode,
            @PathVariable String url) {

        List<ImageUrlResponseDto> dtoList = photoService.getPhoto(roomCode, url);

        return ResponseEntity.ok().body(dtoList);
    }

    @DeleteMapping("/{roomCode}/{url}")
    public ResponseEntity<?> deletePhoto(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestParam String photoId
    ){

        boolean deleted = photoService.deletePhoto(roomCode, url, photoId);

        return ResponseEntity.ok().body(Map.of(
                "successes", deleted));
    }
}
