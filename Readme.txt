This project includes a completely revamped version of the code for
FIRST team 399's 2012 season robot, X-1. The revamping of code was prompted by a review of old code.
This review facilitated a reimplementation of the elements that worked well and a removal of
unnecessary elements. 
Much motivation for this project was through the Japanese philosophy of
Kaizen, or improvement for the better, with an emphasis on cutting out waste and overly hard work.


Rather than listing what is new in this brand new version, it is much easier to list what has stayed the same:
__________________________________________________________________________________________________________________________________
  -Image processing algorithms

  -Shooter velocity control algorithms

  -Initial Configuration of robot components


Highlights of new features:
__________________________________________________________________________________________________________________________________
  -Text file scripted autonomous - Just drop a special text file onto the cRIO and autonomous will follow the instructions in that file

  -Brand new image tracking algorithm - Uses a single image and some trigonometry to determine the position of the target and where to turn the turret

  -Simplified structure of the program

  -Various controllers for automating tasks as aiming, shooting, and balancing. The auto shoot controller makes shots more reliable as it disables the ability to shoot until the shooter is at target speed

  -Much more




Other files included in this project:
__________________________________________________________________________________________________________________________________
  -A sample autonomous - Sample autonomous program written using the new system of text file reading

  -A writeup - Writeup created to help explain the robot code's creation, operation, algorithms, and thought processes in one concise document

  -A series of test plans - A series of plans for testing the robot in various situations




__________________________________________________________________________________________________________________________________
If you have any questions, contact me:

Jeremy Paul Germita

Jeremy.Germita@gmail.com