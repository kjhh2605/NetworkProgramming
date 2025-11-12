package resource;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GifBackgroundRemover {

    private final GifUtil gifUtil;

    public GifBackgroundRemover() {
        this.gifUtil = new GifUtil();
    }

    /**
     * GIF 애니메이션의 모든 프레임에서 배경색을 제거합니다.
     */
    public void removeBackground(String inputPath, String outputPath) throws IOException {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);

        // GIF 리더 생성
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream input = ImageIO.createImageInputStream(inputFile);
        reader.setInput(input);

        int numFrames = reader.getNumImages(true);
        System.out.println("총 " + numFrames + "개의 프레임을 처리합니다...");

        // GIF 라이터 생성
        ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
        ImageOutputStream output = ImageIO.createImageOutputStream(outputFile);
        writer.setOutput(output);
        writer.prepareWriteSequence(null);

        // 각 프레임 처리
        for (int i = 0; i < numFrames; i++) {
            System.out.println("프레임 " + (i + 1) + "/" + numFrames + " 처리 중...");

            BufferedImage frame = reader.read(i);
            IIOMetadata originalMetadata = reader.getImageMetadata(i);

            // 배경 제거
            BufferedImage transparentFrame = gifUtil.makeTransparent(frame);

            // 새 메타데이터 생성
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            IIOMetadata metadata = writer.getDefaultImageMetadata(
                    ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB),
                    writeParam);

            // 원본 메타데이터에서 설정 복사
            try {
                String metaFormatName = metadata.getNativeMetadataFormatName();
                IIOMetadataNode root = (IIOMetadataNode) originalMetadata.getAsTree(metaFormatName);

                // 투명도 설정
                IIOMetadataNode graphicsControlExtension = getOrCreateNode(root, "GraphicControlExtension");
                graphicsControlExtension.setAttribute("transparentColorFlag", "TRUE");
                graphicsControlExtension.setAttribute("transparentColorIndex", "0");

                metadata.setFromTree(metaFormatName, root);
            } catch (Exception e) {
                System.out.println("메타데이터 설정 경고: " + e.getMessage());
            }

            // 프레임 쓰기
            IIOImage iioImage = new IIOImage(transparentFrame, null, metadata);
            writer.writeToSequence(iioImage, null);
        }

        writer.endWriteSequence();
        output.close();
        input.close();

        System.out.println("배경 제거 완료: " + outputPath);
    }

    /**
     * 메타데이터 트리에서 노드를 찾거나 생성합니다.
     */
    private IIOMetadataNode getOrCreateNode(IIOMetadataNode root, String nodeName) {
        for (int i = 0; i < root.getLength(); i++) {
            if (root.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) root.item(i);
            }
        }

        // 노드가 없으면 생성
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        root.appendChild(node);
        return node;
    }

    public static void main(String[] args) {
        GifBackgroundRemover remover = new GifBackgroundRemover();

        try {
            String inputPath = "img/skill1.gif";
            String outputPath = "img/skill1_transparent.gif";

            remover.removeBackground(inputPath, outputPath);

        } catch (IOException e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
