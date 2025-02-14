package com.fish.shareplan.domain.checklist.entity;

import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.domain.room.entity.Room;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_checklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "checkListItem")
@Builder
public class CheckList {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @Column(name = "category")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "created_at", nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private CheckListItem checkListItem;

    public void update(CheckListRequestDto dto, CheckListItem checkListItem){
        this.checkListItem = checkListItem;
        this.category = dto.getCategory();
    }
}
