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
  loading = true;

  constructor(private s3: S3Service) {}
  /* eslint-disable */
  async ngOnInit(): Promise<any> {
    if (this.tire?.imageUrl) {
      try {
        this.imageSrc = await this.s3.getImageS3(this.tire.imageUrl);
      } catch (error) {
        this.imageSrc = './content/images/website_icon_pack/icon_pack/image_not_found.jpg';
      }
    } else {
      this.imageSrc = './content/images/website_icon_pack/icon_pack/image_not_found.jpg';
    }
    this.loading = false; // stop loading once image is set
  }
}
