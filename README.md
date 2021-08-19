# Picture-To-Minecraft-Skin
###### This is a java program that I created which allows you to create a skin for the game Minecraft from an image.

### About this program

This is a java program that uses JavaFX, I made this because I was looking for something that I could make to refresh my knowledge of JavaFX and also some basic image editing.  
It was made using `jdk-16.0.2` and `javafx-sdk-16`

#### Features

While the main functionality of the program is to essentially paste an image onto the front of a character it also has some other options

* Double sided
    * This allows you to paste the image on the back of the skin as well.
    
* Project Limbs
    * This will project the front of the arms, legs, head and body to the other sides of them and also fill in the tops and bottoms of the character's legs, arms, body and head.  
    This can be quite useful for photo-bashing a skin together, then adding a face and more details in an external program.


### How to use
1. Download the `Picture_to_Minecraft_Skin.jar` file.
1. Open the .jar file using Java SE, this will launch the program. Once it is open, click "Open Image" in the top left.  
Then navigate to the image that you want to use.  
1. Once the image opens you can see a preivew of the front of the skin on the right, and a small preview of the image that the program is using to get the pixels from.  
If the smaller image is warped or off, you may have to resize it for the best result.
1. Now that the skin is loaded, you can have 3 options that you can use
    1. Set the RGB values of the skin, these will be the back, sides, tops and bottoms of the limbs that are not the front faces.  
    (any illegal values such as a letter, a number that is less than 0 or greater than 255, will default to 128 [the average of 0 and 255].)
    1. Double sided can be selected to make the image appear on the back aswell as the front.
    1. Project limbs will project the front side of the body parts to be projected onto all sides of their limbs, body and head.   
    An example would be having a horizontal stripe on an arm, it will be projected onto all sides of said arm  
    resulting in a complete band around the arm.
    1. An important note, both double sided and projected can be used at once, the double sided will overwrite the projected     face.
1. Once you've configured your options you can choose to name the skin. The file will be written to the desktop under that name.  
If no name is provided it will be named `output_skin.png`.
   
**Things to keep in mind**  
* The characters default size is 16px wide and 32px high, so try use an image that has a ratio of 1:2.
* If there is a specific area of an image you want to use, make sure that it is as large as possible, that way you'll get the best results.
* Transparent images will not be transparent. It will process them but the transparent areas will preview as black and will output as white.  
I recommend changing the transparent areas to a desired colour, then using it.

### The code
All the source code is available, feel free to do whatever you'd like with it!

### Example

| Example Image     | Result              |
| -------------     |:-------------:      |
| ![exampleImage1](/.public/exampleImage1.png "Skin Preview1")|![exampleImage1Result](/.public/example1Result.png "Skin Preview1 Result")|
| ![exampleImage2](/.public/exampleImage2.png "Skin Preview2")|![exampleImage2Result](/.public/example2Result.png "Skin Preview2 Result")|
| ![exampleImage3](/.public/exampleImage3.png "Skin Preview3")|![exampleImage3Result](/.public/example3Result.PNG "Skin Preview3 Result")|



