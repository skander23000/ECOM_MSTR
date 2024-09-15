import { CommonModule } from '@angular/common';

export class ImageComponent {}
import { Component, Input, OnInit } from '@angular/core';
import { S3Service } from '../s3.service';
import { ITire } from '../entities/tire/tire.model';

@Component({
  selector: 'jhi-image',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './image.component.html',
  styleUrl: './image.component.scss',
})
export class TireImageComponent implements OnInit {
  @Input() tire: ITire | null = null;
  imageSrc: string | null = null;

  constructor(private s3: S3Service) {}

  ngOnInit(): void {
    (async () => {
      if (this.tire?.imageUrl) {
        this.imageSrc = await this.s3.getImageS3(this.tire.imageUrl);
      } else {
        this.imageSrc = './content/images/website_icon_pack/icon_pack/image_not_found.png';
      }
    })();
  }
}
