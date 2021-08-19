package PTMS;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Controller {
    public TextField fileName,redVal, blueVal, greenVal;
    public CheckBox doubleSidedOption, projectLimbsOption;
    public Text errorText;
    private Window stage;
    public ImageView uploadedImage, previewImage;
    private static BufferedImage pictureToBeConverted;

    public void openImage(ActionEvent actionEvent) throws IOException {
        errorText.setText("");

        //Set Up file explorer and ask for a file to be gotten

        FileChooser input_file = new FileChooser();
        input_file.setTitle("Choose an Image");
        //Set up empty image to store image
        BufferedImage image;
        //Try to read in the image, catches if the explorer is closed
        try{
            image = ImageIO.read(input_file.showOpenDialog(stage));
        }catch(IllegalArgumentException | NullPointerException except){
            //checks if the file explorer is closed and changes the error text
            errorText.setFill(javafx.scene.paint.Color.RED);
            errorText.setText("Image opening canceled");
            errorText.setFont(Font.font(18));
            return;
        }
        //Making a temperary image to scale up the preview of the input image
        Image tmp;
        try {
            //makes sure that the file selected is an image as it will throw an error.
            tmp = image.getScaledInstance(16, 32, BufferedImage.SCALE_DEFAULT);
        }catch (NullPointerException ex){
            //updating error text
            errorText.setFill(javafx.scene.paint.Color.RED);
            errorText.setText("File is not an image");
            errorText.setFont(Font.font(18));
            return;
        }
        //Create a new and resize the buffered image using g2d
        BufferedImage dimg = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        image = dimg;

        //Convert to jfx image and display it, upscale the image to reduce the blurry filtering effect
        uploadedImage.setImage(SwingFXUtils.toFXImage(UpscaleImage(image,3),null));
        pictureToBeConverted = image;
        //previewImage.setFitHeight(1);
        //Display the preview of the front of the skin.
        previewImage.setImage(SwingFXUtils.toFXImage((UpscaleImage(GeneratePreview(image),6)),null));

    }

    public void GenerateSkin(ActionEvent actionEvent) throws IOException {
        //Clears error text
        errorText.setText("");
        if(pictureToBeConverted == null){
            //if no image is uploaded update error text
            errorText.setFill(javafx.scene.paint.Color.RED);
            errorText.setText("Error no Image Uploaded");
            errorText.setFont(Font.font(18));
            return;
        }
        //loading in skin template
        BufferedImage skinTemplate = ImageIO.read(getClass().getResourceAsStream("skinTemplate.png"));
        //get an array of the RGB values that are to be used in the areas of the skin without images, this also error checks them.
        int[] backgroundColours = ColorInputCheck();
        //System.out.println("Red: "+backgroundColours[0] + "\nGreen: " + backgroundColours[1]+"\nBlue: "+backgroundColours[2]);
        skinTemplate = FlatColorImage(skinTemplate,backgroundColours[0],backgroundColours[1],backgroundColours[2]);

        //uses the skin template to transfer the pixels from the preview image to the front faces of the skin template, skinTemplate is the final skin and it constantly gets updated
        //Front
        //Body parts are colour coded in the skin template, used for getting the coordinates.
        //The first 2 values are the top left position of the area to cut, the next 2 are the bottom right of the area.
        //The next 2 values are the image given, which the coordinates are used on to cut the area, the next is the image that it's being applied to.
        //The final values ar the x,y coordinates of the skin template the cut area is being applied to.
        skinTemplate = MoveImageToImage(4,0,11,7,pictureToBeConverted,skinTemplate,8,8); // orange head
        skinTemplate = MoveImageToImage(4,8,11,19,pictureToBeConverted,skinTemplate,20,20); // yellow body
        skinTemplate = MoveImageToImage(4,20,7,31,pictureToBeConverted,skinTemplate,4,20); // green right leg
        skinTemplate = MoveImageToImage(0,8,3,19,pictureToBeConverted,skinTemplate,44,20); // red right arm
        skinTemplate = MoveImageToImage(8,20,11,31,pictureToBeConverted,skinTemplate,20,52); // black left leg
        skinTemplate = MoveImageToImage(12,8,15,19,pictureToBeConverted,skinTemplate,36,52); // blue left arm

        if(projectLimbsOption.isSelected()){
            //Only do 3 as the first face was done at the start
            //Main body faces
            //These transfer the front of the selected arm and applies it to all the faces of that arm, excluding the top and bottom
            skinTemplate = MoveImageToImage(0, 8, 3, 19, pictureToBeConverted, skinTemplate, 52, 20); // red right arm back
            skinTemplate = MoveImageToImage(0, 8, 3, 19, pictureToBeConverted, skinTemplate, 40, 20); // red right arm left
            skinTemplate = MoveImageToImage(0, 8, 3, 19, pictureToBeConverted, skinTemplate, 48, 20); // red right arm right


            skinTemplate = MoveImageToImage(12,8,15,19,pictureToBeConverted,skinTemplate,32,52); // blue left arm left
            skinTemplate = MoveImageToImage(12,8,15,19,pictureToBeConverted,skinTemplate,40,52); // blue left arm back
            skinTemplate = MoveImageToImage(12,8,15,19,pictureToBeConverted,skinTemplate,44,52); // blue left arm right


            skinTemplate = MoveImageToImage(8,20,11,31,pictureToBeConverted,skinTemplate,16,52); // black left leg
            skinTemplate = MoveImageToImage(8,20,11,31,pictureToBeConverted,skinTemplate,24,52); // black left leg
            skinTemplate = MoveImageToImage(8,20,11,31,pictureToBeConverted,skinTemplate,28,52); // black left leg


            skinTemplate = MoveImageToImage(4,20,7,31,pictureToBeConverted,skinTemplate,0,20); // green right leg
            skinTemplate = MoveImageToImage(4,20,7,31,pictureToBeConverted,skinTemplate,8,20); // green right leg
            skinTemplate = MoveImageToImage(4,20,7,31,pictureToBeConverted,skinTemplate,12,20); // green right leg


            skinTemplate = MoveImageToImage(4,0,11,7,pictureToBeConverted,skinTemplate,0,8); // orange head left
            skinTemplate = MoveImageToImage(4,0,11,7,pictureToBeConverted,skinTemplate,16,8); // orange head back
            skinTemplate = MoveImageToImage(4,0,11,7,pictureToBeConverted,skinTemplate,24,8); // orange head right
            skinTemplate = MoveImageToImage(4,0,11,7,pictureToBeConverted,skinTemplate,8,0); // orange head top
            skinTemplate = MoveImageToImage(4,0,11,7,pictureToBeConverted,skinTemplate,16,0); // orange head bottom

            skinTemplate = MoveImageToImage(4,8,7,19,pictureToBeConverted,skinTemplate,28,20); // yellow body left
            skinTemplate = MoveImageToImage(8,8,11,19,pictureToBeConverted,skinTemplate,16,20); // yellow body right

            //Bottom of chest arms legs
            //chest top & bottom
            //The top pixels on the arm are repeated across the top and bottom pixels are repeated across the bottom.
            skinTemplate = MoveImageToImage(4,8,11,8,pictureToBeConverted,skinTemplate,20,19); // top outer
            skinTemplate = MoveImageToImage(4,9,11,9,pictureToBeConverted,skinTemplate,20,18); // top inner
            skinTemplate = MoveImageToImage(4,8,11,8,pictureToBeConverted,skinTemplate,20,16); // top outer
            skinTemplate = MoveImageToImage(4,9,11,9,pictureToBeConverted,skinTemplate,20,17); // top inner

            skinTemplate = MoveImageToImage(4,19,11,19,pictureToBeConverted,skinTemplate,28,19); // bottom outer
            skinTemplate = MoveImageToImage(4,19,11,19,pictureToBeConverted,skinTemplate,28,18); // bottom inner
            skinTemplate = MoveImageToImage(4,19,11,19,pictureToBeConverted,skinTemplate,28,16); // bottom outer
            skinTemplate = MoveImageToImage(4,19,11,19,pictureToBeConverted,skinTemplate,28,17); // bottom inner

            //arms top & bottom
            //Arms
            //right top
            skinTemplate = MoveImageToImage(0,8,3,8,pictureToBeConverted,skinTemplate,44,19); // top ring
            skinTemplate = MoveImageToImage(0,8,3,8,pictureToBeConverted,skinTemplate,44,18); // top ring
            skinTemplate = MoveImageToImage(0,8,3,8,pictureToBeConverted,skinTemplate,44,17); // top ring
            skinTemplate = MoveImageToImage(0,8,3,8,pictureToBeConverted,skinTemplate,44,16); // top ring
            //left top
            skinTemplate = MoveImageToImage(12,8,15,8,pictureToBeConverted,skinTemplate,36,51); // bottom ring
            skinTemplate = MoveImageToImage(12,8,15,8,pictureToBeConverted,skinTemplate,36,50); // bottom ring
            skinTemplate = MoveImageToImage(12,8,15,8,pictureToBeConverted,skinTemplate,36,49); // bottom ring
            skinTemplate = MoveImageToImage(12,8,15,8,pictureToBeConverted,skinTemplate,36,48); // bottom ring

            //right bottom
            skinTemplate = MoveImageToImage(0,19,3,19,pictureToBeConverted,skinTemplate,48,19); // bottom ring
            skinTemplate = MoveImageToImage(0,19,3,19,pictureToBeConverted,skinTemplate,48,18); // bottom ring
            skinTemplate = MoveImageToImage(0,19,3,19,pictureToBeConverted,skinTemplate,48,17); // bottom ring
            skinTemplate = MoveImageToImage(0,19,3,19,pictureToBeConverted,skinTemplate,48,16); // bottom ring
            //left bottom
            skinTemplate = MoveImageToImage(12,19,15,19,pictureToBeConverted,skinTemplate,40,51); // bottom ring
            skinTemplate = MoveImageToImage(12,19,15,19,pictureToBeConverted,skinTemplate,40,50); // bottom ring
            skinTemplate = MoveImageToImage(12,19,15,19,pictureToBeConverted,skinTemplate,40,49); // bottom ring
            skinTemplate = MoveImageToImage(12,19,15,19,pictureToBeConverted,skinTemplate,40,48); // bottom ring

            //Legs

            //Right Leg Top
            skinTemplate = MoveImageToImage(4,20,7,20,pictureToBeConverted,skinTemplate,4,16); // bottom ring
            skinTemplate = MoveImageToImage(4,20,7,20,pictureToBeConverted,skinTemplate,4,17); // bottom ring
            skinTemplate = MoveImageToImage(4,20,7,20,pictureToBeConverted,skinTemplate,4,18); // bottom ring
            skinTemplate = MoveImageToImage(4,20,7,20,pictureToBeConverted,skinTemplate,4,19); // bottom ring


            //Left leg Top
            skinTemplate = MoveImageToImage(8,20,11,20,pictureToBeConverted,skinTemplate,20,48); // bottom ring
            skinTemplate = MoveImageToImage(8,20,11,20,pictureToBeConverted,skinTemplate,20,49); // bottom ring
            skinTemplate = MoveImageToImage(8,20,11,20,pictureToBeConverted,skinTemplate,20,50); // bottom ring
            skinTemplate = MoveImageToImage(8,20,11,20,pictureToBeConverted,skinTemplate,20,51); // bottom ring


            //right Leg bottom
            skinTemplate = MoveImageToImage(4,31,7,31,pictureToBeConverted,skinTemplate,8,16); // bottom ring
            skinTemplate = MoveImageToImage(4,31,7,31,pictureToBeConverted,skinTemplate,8,17); // bottom ring
            skinTemplate = MoveImageToImage(4,31,7,31,pictureToBeConverted,skinTemplate,8,18); // bottom ring
            skinTemplate = MoveImageToImage(4,31,7,31,pictureToBeConverted,skinTemplate,8,19); // bottom ring

            //Left Leg Bottom
            skinTemplate = MoveImageToImage(8,31,11,31,pictureToBeConverted,skinTemplate,24,48); // bottom ring
            skinTemplate = MoveImageToImage(8,31,11,31,pictureToBeConverted,skinTemplate,24,49); // bottom ring
            skinTemplate = MoveImageToImage(8,31,11,31,pictureToBeConverted,skinTemplate,24,50); // bottom ring
            skinTemplate = MoveImageToImage(8,31,11,31,pictureToBeConverted,skinTemplate,24,51); // bottom ring
            skinTemplate = MoveImageToImage(4, 8, 11, 19, pictureToBeConverted, skinTemplate, 32, 20); // yellow body
            //Back
            //This applies the front image to the back, making the skin double sided, i.e the back is the same as the front

        }

        if(doubleSidedOption.isSelected()) {
            skinTemplate = MoveImageToImage(4, 0, 11, 7, pictureToBeConverted, skinTemplate, 24, 8); // orange head
            skinTemplate = MoveImageToImage(4, 8, 11, 19, pictureToBeConverted, skinTemplate, 32, 20); // yellow body
            skinTemplate = MoveImageToImage(4, 20, 7, 31, pictureToBeConverted, skinTemplate, 28, 52); // green right leg
            skinTemplate = MoveImageToImage(0, 8, 3, 19, pictureToBeConverted, skinTemplate, 44, 52); // blue left arm
            skinTemplate = MoveImageToImage(8, 20, 11, 31, pictureToBeConverted, skinTemplate, 12, 20); // black left leg
            skinTemplate = MoveImageToImage(12, 8, 15, 19, pictureToBeConverted, skinTemplate, 52, 20); // red right arm
        }
//        //Sides
//        if(doubleSidedOption.isSelected()) {
//            skinTemplate = MoveImageToImage(4, 0, 11, 7, pictureToBeConverted, skinTemplate, 0, 8); // orange head
//            skinTemplate = MoveImageToImage(4, 0, 11, 7, pictureToBeConverted, skinTemplate, 16, 8); // orange head
//            skinTemplate = MoveImageToImage(6, 20, 9, 31, pictureToBeConverted, skinTemplate, 0, 20); // green right leg
//            skinTemplate = MoveImageToImage(6, 20, 9, 31, pictureToBeConverted, skinTemplate, 8, 20); // green right leg
//            skinTemplate = MoveImageToImage(6, 20, 9, 31, pictureToBeConverted, skinTemplate, 16, 52); // green right leg
//            skinTemplate = MoveImageToImage(6, 20, 9, 31, pictureToBeConverted, skinTemplate, 24, 52); // green right leg
//            skinTemplate = MoveImageToImage(6, 8, 9, 19, pictureToBeConverted, skinTemplate, 40, 20); // red right arm
//            skinTemplate = MoveImageToImage(6, 8, 9, 19, pictureToBeConverted, skinTemplate, 48, 20); // red right arm
//            skinTemplate = MoveImageToImage(6, 8, 9, 19, pictureToBeConverted, skinTemplate, 32, 52); // red right arm
//            skinTemplate = MoveImageToImage(6, 8, 9, 19, pictureToBeConverted, skinTemplate, 40, 52); // red right arm
//
//        }
        //Gets the location of the desktop, where the skin is going to be saved to.
        String desktopLocation = System.getProperty("user.home")+"/Desktop/";
        //re-sizes font to reduce shifting the layout.
        errorText.setFont(Font.font(10));
        //If there is no filename it defaults the name to skin_output.png
        if(fileName.getText().equals("")) {
            //System.out.println(desktopLocation+"skin_output.png");
            ImageIO.write(skinTemplate, "png", new File(desktopLocation+"skin_output.png"));
            //ImageIO.write(pictureToBeConverted, "png", new File(desktopLocation+"skin_output_test.png"));
            //update error text to say that the skin was written to desktop and gives the name.
            errorText.setFill(javafx.scene.paint.Color.LIGHTGREEN);
            errorText.setText("Image Saved To Desktop under skin_output_test.png");
        }else{
            //writes the file to the desktop and uses a specified name given by the user.
            ImageIO.write(skinTemplate, "png", new File(desktopLocation+fileName.getText()+".png"));
            errorText.setFill(javafx.scene.paint.Color.LIGHTGREEN);
            errorText.setText("Image Saved To Desktop under "+fileName.getText()+".png");
        }
    }

    public BufferedImage GeneratePreview(BufferedImage image){
        //this returns an image of them laid out as if it were a skin, by blocking out the negative areas, i.e what's not the head arms or legs.
        Color color = new Color(75,75,75);
        image = DrawBox(0,0,3,7,image,color);
        image = DrawBox(12,0,15,7,image,color);
        image = DrawBox(0,20,3,31,image,color);
        return DrawBox(12,20,15,31,image,color);
    }

    public BufferedImage DrawBox(int topLeftX, int topLeftY, int bottomRightX,int bottomRightY, BufferedImage image,Color color) {
        //simple for loop to colour an area.
        for(int  column = topLeftX; column <= bottomRightX; column++) {
            for (int row = topLeftY; row <= bottomRightY; row++) {
                image.setRGB(column,row,color.getRGB());
            }
        }
        return image;
    }

    public BufferedImage MoveImageToImage(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY, BufferedImage imageToExtract, BufferedImage imageToApply, int applyX, int applyY) {
        //this gets the pixels from one image, then transfers them to another
        for(int  column = topLeftX; column <= bottomRightX; column++) {
            for (int row = topLeftY; row <= bottomRightY; row++) {
                imageToApply.setRGB(applyX+column-topLeftX,applyY+row-topLeftY,imageToExtract.getRGB(column,row));
            }
        }
        return imageToApply;
    }

    public BufferedImage FlatColorImage(BufferedImage imageToColor, int red, int green, int blue){
        Color colorToUse = new Color(red,green,blue);
        //Makes all the pixels in an image the same colour.
        for(int  column = 0; column <= imageToColor.getWidth()-1; column++) {
            for (int row = 0; row <= imageToColor.getHeight()-1; row++) {
                //masks the skins "outer shell".
                if(column <= 15 && row >= 32){

                }else if(column >= 32 && row <= 15){

                }else if(column >= 16 && column <=55 && row >= 32 && row <= 47){

                }else if(column >= 48 && row >= 48){

                }else{
                    imageToColor.setRGB(column, row, colorToUse.getRGB());
                }
            }
        }

        return imageToColor;
    }

    private int[] ColorInputCheck(){
        //Creates an empty 3 slot array and 3 variables for the rgb values
        int[] result  = new int[3];
        int red,green,blue;

        //Make sure that the values entered are numbers
        try{
            red = Integer.parseInt(redVal.getText());
        }catch(NumberFormatException ex){
            red = 0;
        }

        try{
            green = Integer.parseInt(greenVal.getText());
        }catch(NumberFormatException ex){
            green = 0;
        }

        try{
            blue = Integer.parseInt(blueVal.getText());
        }catch(NumberFormatException ex){
            blue = 0;
        }

        //Cap the values
        red = ClampNumberUpperLower(red,255,0);
        green = ClampNumberUpperLower(green,255,0);
        blue = ClampNumberUpperLower(blue,255,0);

        //assign the colours into their slots
        result[0] = red;
        result[1] = green;
        result[2] = blue;

        return result;
    }

    private BufferedImage UpscaleImage(BufferedImage image, int scale){
        // used to upscale the images
        //gets width and height, then uses the scale to create a new image with the scaled size
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage returnImage = new BufferedImage(width*scale, height*scale, BufferedImage.TYPE_INT_RGB);

        //Gets the RGB at every pixel in the first image, then draws that colour onto the new image in squares of scale
        //i.e scaling an image up by a factor of 2 it will draw a 2x2 set of pixels in the new image in the same colour as the original pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x,y);
                for (int dy = 0; dy < scale; dy++) {
                    for (int dx = 0; dx < scale; dx++) {
                        returnImage.setRGB(x * scale + dx, y * scale + dy, rgb);
                    }
                }
            }
        }

        return returnImage;
    }

    //Clamps numbers between 2 specified values, returns the average if it's outside the bounds
    private int ClampNumberUpperLower(int number, int upper, int lower){
        if(number>upper || number<lower){
            return ((upper+lower)/2);
        }
        return number;
    }
}
