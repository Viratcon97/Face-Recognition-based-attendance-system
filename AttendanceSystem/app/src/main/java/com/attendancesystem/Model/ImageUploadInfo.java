package com.attendancesystem.Model;

public class ImageUploadInfo{
        public String imageName;

        public String imageURL;

        public ImageUploadInfo() {

        }

        public ImageUploadInfo(String name, String url) {

            this.imageName = name;
            this.imageURL= url;
        }

    public ImageUploadInfo(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageName() {
            return imageName;
        }

        public String getImageURL() {
            return imageURL;
        }

}
