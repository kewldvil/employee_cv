package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ImageData")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String filePath;
    @Lob
    @Column(name = "imagedata",length = 1000)
    private byte[] imageData;


}
