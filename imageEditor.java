import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

class imageEditor {

    public static BufferedImage convertToGrayScale(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }

     public static BufferedImage horizontalInvert(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(width - j - 1, i));
            }
        }
        return outputImage;
    }
    
    public static BufferedImage verticalInvert(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, height - i - 1));
            }
        }
        return outputImage;
    }

    public static int updateValue(int n) {
        int increased = n + n / 5;
        if (increased <= 255)
            return increased;
        return 255;
    }
    
    public static BufferedImage rotateImageClockwise(BufferedImage inputImage) {
        int height = inputImage.getWidth();
        int width = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(i, width - 1 - j));
            }
        }
        return outputImage;
    }

    public static BufferedImage rotateImageAntiClockwise(BufferedImage inputImage) {
        BufferedImage outputImage = rotateImageClockwise(inputImage);
        outputImage = rotateImageClockwise(outputImage);
        outputImage = rotateImageClockwise(outputImage);
        return outputImage;
    }

   

    

    public static BufferedImage increaseBrightness(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = new Color(inputImage.getRGB(j, i));
                int red = updateValue(pixel.getRed());
                int blue = updateValue(pixel.getBlue());
                int green = updateValue(pixel.getGreen());
                Color newPixel = new Color(red, green, blue);
                outputImage.setRGB(j, i, newPixel.getRGB());
            }
        }
        return outputImage;
    }

    public static BufferedImage blurImage(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                int count = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (k >= 0 && k < height && l >= 0 && l < width) {
                            Color pixel = new Color(inputImage.getRGB(l, k));
                            red += pixel.getRed();
                            green += pixel.getGreen();
                            blue += pixel.getBlue();
                            count++;
                        }
                    }
                }
                red /= count;
                green /= count;
                blue /= count;
                Color newPixel = new Color(red, green, blue);
                outputImage.setRGB(j, i, newPixel.getRGB());
            }
        }
        return outputImage;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter filename to edit (Must be in the same folder): ");
        String inputFilePath = scanner.nextLine();
        File inputFile = new File(inputFilePath);
        try {
            BufferedImage inputImage = ImageIO.read(inputFile);
            while (true) {
                System.out.println();
                System.out.println("----- SELECT ONE OF THE FOLLOWING OPTIONS -----");
                System.out.println("1. Grayscale");
                System.out.println("2. Horizontal Flip");
                System.out.println("3. Vertical Flip");
                System.out.println("4. Rotate Clockwise");
                System.out.println("5. Rotate Anticlockwise");
                System.out.println("6. Increase Brightness");
                System.out.println("7. Blur Image");
                System.out.println("q. Quit");
                System.out.print("\nEnter an option: ");
                String choice = scanner.nextLine();
                BufferedImage filtered;
                if (choice.equals("1")) {
                    filtered = convertToGrayScale(inputImage);
                } else if (choice.equals("2")) {
                    filtered = horizontalInvert(inputImage);
                } else if (choice.equals("3")) {
                    filtered = verticalInvert(inputImage);
                } else if (choice.equals("4")) {
                    filtered = rotateImageClockwise(inputImage);
                } else if (choice.equals("5")) {
                    filtered = rotateImageAntiClockwise(inputImage);
                } else if (choice.equals("6")) {
                    filtered = increaseBrightness(inputImage);
                } else if (choice.equals("7")) {
                    filtered = blurImage(inputImage);
                } else if (choice.equals("q")) {
                    System.out.println("Thanks");
                    break;
                } else {
                    System.out.println("Select a valid option");
                    continue;
                }
                System.out.print("Enter the name for output file: ");
                String outFileName = scanner.nextLine();
                File outputFile = new File(outFileName + ".jpg");
                boolean didWrite = ImageIO.write(filtered, "jpg", outputFile);
                System.out.println(didWrite ? "\nEdit Succesfull!" : "\nEdit Failed!");
            }
        } catch (IOException a) {
            System.out.println("Invalid input file!");
        }
        scanner.close();
    }
}
