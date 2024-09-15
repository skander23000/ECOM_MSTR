import { Injectable } from '@angular/core';
import { GetObjectCommand, PutObjectCommand, PutObjectCommandOutput, S3Client } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';
import { environment } from './environment/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class S3Service {
  private s3: S3Client;
  public constructor(private http: HttpClient) {
    this.s3 = new S3Client({
      credentials: {
        accessKeyId: environment.ACCESS_KEY,
        secretAccessKey: environment.SECRET_ACCESS_KEY,
      },
      region: environment.BUCKET_REGION,
    });
  }
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
      return './content/imgages/website_icon_pack/icon_pack/image_not_found.jpg';
    }
  }

  public async uploadImage(file: File): Promise<PutObjectCommandOutput> {
    const contentType = file.type;
    return await this.s3.send(
      new PutObjectCommand({
        Body: file,
        Bucket: environment.BUCKET_NAME,
        Key: file.name,
        ContentType: contentType,
      }),
    );
  }
}
