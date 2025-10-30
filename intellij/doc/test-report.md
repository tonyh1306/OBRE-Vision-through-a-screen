# Test Report — OBRE (CLI-Based Prototype)

## Overview
This report summarizes testing for the command-line prototype of the Object Recognition App. 
The tests focused on verifying that the controller, model, and view interact correctly and that the console outputs match the expected messages for each scenario.

---

## System Tests

**Test 1: Image Recognition Command**  
**Feature:** Object recognition simulation  
**Input:** `vase.jpg`  
**Expected Output:**  
The console should print: A text of what the OpenCVAlgo outputs as a result

Output: 

**Test 2: Text Recognition Command**  
**Feature:** Text recognition prototype  
**Input:** `text` followed by `sign.jpg`  
**Expected Output:**  
The console should print:
Text output is: 'sign.jpg' a tree (example)
This is an example from the controller successfully calling the TextRecognizer Model. 

Output: Text output is: 'sign.jpg' a tree (example)
Test outcome: Success output is as expected

**Test 3: Invalid Input**  
**Feature:** Input validation and error handling  
**Input:** Random string (`nonsense`)  
**Expected Output:**  
The application should display an error message and exit the program

Output:
The system asks again and again until the input is valid 
or the user wants the exit the prototype. This is true for the text-recognition as well as the image_recognotion. If the input is 
empty then the prototype exits and stops.
Test Outcome: Success output is as expected. 


## Conclusion
All command-line system tests passed successfully.
