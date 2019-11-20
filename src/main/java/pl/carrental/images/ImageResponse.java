package pl.carrental.images;

public class ImageResponse {
    private String status;
    private String fileName;

    public ImageResponse(String status, String fileName) {
        this.status = status;
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
