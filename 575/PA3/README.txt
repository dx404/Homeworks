COMP 575 Spring 2013: Programmming Assignment 3: OpenGL Rasterization

Student : Duo Zhao
Email   : duo.zhao@unc.edu

Development Enviorment: 
    Operating System    : Macintosh OS X 10.8.3 (Mountain Lion)
    IDE Tools           : Xcode Version 4.6.1 (4H512)
    Compiler            : Apple LLVM 4.2 (clang)

Linker Library/framework
    GLUT.framework
    OpenGL.framework

Working Directory: The current project folder
    
Running Arugment: 
    The program could take two arguments other the program name
    The first argument specifies the drawing method index, i.e. 
        1 -imme_draw - is for rendering the bunny using OpenGL's immediate mode
        2 -vao_vbo_draw - is for rendering the bunny using VAOs and VBOs
        3 -ele_draw is - for testing using glDrawElement() with neither VAOs nor VBOs
        4 -beta is for - testing, using OpenGL build-in functions
        
    The second argument specifies whether the timer is enabled or not 
            1       - enable the timer
        non-zero    - disable the timer (default)
    
    Without feed the program with arguments, the default drawing method and timer status 
    may be configured in the config.h file. 
    
Questions and issues:
    Since the timer does not work well, I utilized an application on Mac - OpenGL Profiler to monitor the FPS
    
The experiment shows that
    
    For immediate-mode drawing:
        The peak value is 172.1 FPS
        
    For VAO-VBO mode of drawing
        The peak value is 220.0 FPS
    




