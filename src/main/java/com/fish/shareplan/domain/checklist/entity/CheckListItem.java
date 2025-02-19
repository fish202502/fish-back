package com.fish.shareplan.domain.checklist.entity;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_checklist_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckListItem {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CheckListCategory category;

    @Column (name = "assignee")
    private String assignee;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Builder.Default
    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(CheckListRequestDto dto){
        this.isChecked = dto.getIsChecked();
        this.assignee = dto.getAssignee();
        this.content = dto.getContent();
    }

    public static CheckListItemResponseDto toDto(CheckListItem checkListItem){
        return CheckListItemResponseDto.builder()
                .checkListItemId(checkListItem.getId())
                .category(checkListItem.getCategory().getContent())
                .assignee(checkListItem.getAssignee())
                .content(checkListItem.getContent())
                .isChecked(checkListItem.getIsChecked())
                .build();
    };

}
