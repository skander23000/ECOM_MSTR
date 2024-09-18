import { Injectable } from '@angular/core';
import { environment } from './environment/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class S3Service {
  public constructor(private http: HttpClient) {}
  extractFileName(filePath: string | undefined): string {
    return filePath?.split('\\').pop() ?? '';
  }

  public async getImageS3(imageName: string | undefined): Promise<any> {
    const name = this.extractFileName(imageName);
    try {
      const response = await fetch(`https://${environment.BUCKET_NAME}.s3.${environment.BUCKET_REGION}.amazonaws.com/${name}`);
      // Vérification explicite du statut de la réponse
      if (!response.ok) {
        return './content/images/website_icon_pack/icon_pack/image_not_found.jpg';
      }
      return response.url;
    } catch (e: any) {
      return './content/images/website_icon_pack/icon_pack/image_not_found.jpg';
    }
  }
}
