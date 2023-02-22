# QR-Quest

## Notice
- To change the start screen navigation, go to AndroidManifest file
- The Components Activity is used to show some components in the UI design as an example
- All the necessary icons are already created in the drawable file
- All the necessary colours are already created in the colors.xml file (in values folder)
- All the font is also created
- Some buttons' margin/ padding should be changed accordingly. The sample code is not the final code.

## Font (Poppins)
- There are 6 types of fonts:
- Heading 1 (52, Semibold)
- Heading 2 (48, Semibold)
- Heading 3 (40, Semibold)
- Heading 4 (32, Semibold)
- Body      (24, Semibold)
- Small     (16, Semibold) (Most popular)
- To use the fonts, command "TextAppearance", all the font styles will show up accordingly

## Requirements
- Try to use as much [Google Material](https://m3.material.io/) as possible to create components 
- Use Constraint Layout for all xml file (You can use linear layout/ frame layout inside constraint layout, no worries)
- Anything too hard to code, notify the members (using the project board to communicate is recommended)

## Procedure
1. Create an activity with a layout file
2. Create the components (You should look at the example components)
3. All the Shape Appearance, Coloring, Padding, Marign, Sizing should be done by creating a new style section with a unique, precise name in the style.xml file. 
Above each  of the styling section, comment  one line to indicate which component uses that styling section. Example:
![image](https://user-images.githubusercontent.com/90273567/220496638-81228881-a4c4-42c3-91d6-a8a076dd2bf5.png)

4. This is what your components should look like in the main xml file after applying the styling (most lines are logical structuring, styling is separated, making the
file much easier to intepret):
![image](https://user-images.githubusercontent.com/90273567/220496831-bb1d7505-cb95-4b8c-b88f-bfce78e9364a.png)

5. Do not forget to commit regularly. When finish, push the code to your branch and create a pull request. Project board will be updated later (Please remember the
naming conventions for branch) (DO NOT PUSH DIRECTLY TO DEVELOP BRANCH OR MAIN BRANCH, ONLY PUSH CODE TO YOUR BRANCH). If you do not have a branch, create one using 
`git checkout -b <branch name with conventions>`
