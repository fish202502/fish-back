package com.fish.shareplan.service;


import com.fish.shareplan.domain.photo.dto.respnse.ImageUrlResponseDto;
import com.fish.shareplan.domain.photo.entity.ImageUrl;
import com.fish.shareplan.domain.photo.entity.Photo;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.repository.ImageUrlRepository;
import com.fish.shareplan.repository.PhotoRepository;
import com.fish.shareplan.repository.RoomRepository;
import com.fish.shareplan.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PhotoService {

    private final FileUploadUtil fileUploadUtil;  // 로컬

    private final RoomRepository roomRepository;

    private final RoomService roomService;

    private final PhotoRepository photoRepository;
    private final ImageUrlRepository imageUrlRepository;

    //이미지 등록
    public List<ImageUrlResponseDto> addPhoto(String roomCode, String url, List<MultipartFile> images) {
        Room room = roomService.isValid(roomCode, url);

        Photo foundPhoto = photoRepository.findByRoomId(room.getId()).orElse(null);

        if(foundPhoto != null){
            List<ImageUrl> imageUrls = processImages(images, foundPhoto);
            return imageUrls.stream().map(ImageUrl::toDto).toList();
            // 처음 생성
        }else{
            Photo photo = Photo.builder().room(room).build();
            photoRepository.save(photo);
            List<ImageUrl> imageUrls = processImages(images, photo);
            return imageUrls.stream().map(ImageUrl::toDto).toList();
        }
    }


    // 이미지 처리 메서드
    private List<ImageUrl> processImages(List<MultipartFile> images,Photo photo) {
        log.debug("start process Image!!");

        List<ImageUrl> urlList = new ArrayList<>();
        // 이미지들을 서버(/upload 폴더)에 저장
        if (images != null && !images.isEmpty()) {
            log.debug("save process Image!!");

            int order = 1; // 이미지 순서
            for (MultipartFile image : images) {
                // 파일 서버에 저장
                String uploadedUrl = fileUploadUtil.saveFile(image);

                // 이미지들을 데이터베이스 post_images 테이블에 insert
                log.debug("success to save file at: {}", uploadedUrl);

                // 이미지들을 데이터베이스 post_images 테이블에 insert
                ImageUrl imageUrl = ImageUrl.builder()
                        .photoAlbum(photo)
                        .imageUrl(uploadedUrl)
                        .build();

                imageUrlRepository.save(imageUrl);
                urlList.add(imageUrl);
            }
        }
        return urlList;
    }
}
