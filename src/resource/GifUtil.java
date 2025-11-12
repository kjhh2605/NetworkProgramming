package resource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GifUtil {

    /**
     * GIF 이미지의 배경을 투명하게 만듭니다.
     * 원본 투명도를 보존하면서 지정된 색상을 투명하게 처리합니다.
     */
    public BufferedImage makeTransparent(BufferedImage image) {
        if (image == null) {
            return null;
        }

        BufferedImage transparent = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = transparent.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // 배경색 감지 (좌상단 픽셀을 배경색으로 간주)
        int backgroundColor = image.getRGB(0, 0) & 0x00FFFFFF;

        // 유사 색상 임계값 (색상 유사도 허용 범위)
        int threshold = 30;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = transparent.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int rgb = pixel & 0x00FFFFFF;

                // 이미 투명한 픽셀은 그대로 유지
                if (alpha == 0) {
                    continue;
                }

                // 흰색 또는 배경색과 유사한 색상을 투명하게 처리
                if (isWhiteOrSimilar(rgb, threshold) ||
                    isColorSimilar(rgb, backgroundColor, threshold)) {
                    transparent.setRGB(x, y, 0x00000000);
                } else {
                    // 기존 알파값 보존하면서 RGB 설정
                    transparent.setRGB(x, y, pixel);
                }
            }
        }

        return transparent;
    }

    /**
     * 색상이 흰색 또는 밝은 색상인지 확인
     */
    private boolean isWhiteOrSimilar(int rgb, int threshold) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        // 흰색과의 거리 계산
        int dist = Math.abs(255 - r) + Math.abs(255 - g) + Math.abs(255 - b);
        return dist <= threshold * 3;
    }

    /**
     * 두 색상이 유사한지 확인
     */
    private boolean isColorSimilar(int rgb1, int rgb2, int threshold) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = rgb1 & 0xFF;

        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = rgb2 & 0xFF;

        // 색상 간 거리 계산 (맨해튼 거리)
        int dist = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
        return dist <= threshold * 3;
    }

    /**
     * 특정 색상을 투명하게 만듭니다 (크로마키 방식)
     */
    public BufferedImage removeColor(BufferedImage image, Color colorToRemove, int tolerance) {
        if (image == null) {
            return null;
        }

        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int targetRGB = colorToRemove.getRGB() & 0x00FFFFFF;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int rgb = pixel & 0x00FFFFFF;

                if (isColorSimilar(rgb, targetRGB, tolerance)) {
                    result.setRGB(x, y, 0x00000000); // 투명
                } else {
                    result.setRGB(x, y, pixel);
                }
            }
        }

        return result;
    }
}
