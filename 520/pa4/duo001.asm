  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        10
  3         STORE        3[LB]
  4         LOADL        2
  5         LOAD         3[LB]
  6         CALL         mult    
  7         LOADL        100
  8         LOADL        2
  9         CALL         div     
 10         CALL         add     
 11         CALL         putint  
 12         CALL         puteol  
 13         RETURN (0)   1
 14  L11:   LOADL        -1
 15         LOADL        1
 16         LOADA        1[CB]
 17         LOADL        -1
 18         CALL         L10
 19         HALT   (4)   
 20         HALT   (0)   
