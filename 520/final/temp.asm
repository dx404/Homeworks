  0         JUMP         L15
  1  L10:   LOADL        0
  2         LOADL        0
  3         LOADL        1
  4         LOADL        2
  5         JUMP         L12
  6  L11:   LOAD         3[LB]
  7         LOAD         5[LB]
  8         CALL         add     
  9         STORE        3[LB]
 10         LOAD         4[LB]
 11         LOAD         6[LB]
 12         CALL         add     
 13         STORE        4[LB]
 14         LOAD         5[LB]
 15         LOADL        1
 16         CALL         add     
 17         STORE        5[LB]
 18         LOAD         6[LB]
 19         LOADL        2
 20         CALL         add     
 21         STORE        6[LB]
 22  L12:   LOAD         5[LB]
 23         LOADL        100
 24         CALL         le      
 25         JUMPIF (0)   L13
 26         LOAD         6[LB]
 27         LOADL        200
 28         CALL         le      
 29         JUMP         L14
 30  L13:   LOADL        0
 31  L14:   JUMPIF (1)   L11
 32         LOAD         3[LB]
 33         CALL         putint  
 34         CALL         puteol  
 35         LOAD         4[LB]
 36         CALL         putint  
 37         CALL         puteol  
 38         RETURN (0)   1
 39  L15:   LOADL        -1
 40         LOADL        1
 41         LOADA        1[CB]
 42         LOADL        -1
 43         CALL         L10
 44         HALT   (0)   
