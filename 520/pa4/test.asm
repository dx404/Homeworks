  0         JUMP         L11
  1  L10:   LOAD         0[OB]
  2         HALT   (4)   
  3         RETURN (1)   0
  4  L11:   LOADL        -1
  5         LOADL        1
  6         LOADA        1[CB]
  7         JUMP         L13
  8  L12:   LOAD         0[OB]
  9         LOADL        11
 10         HALT   (4)   
 11         CALL         add     
 12         RETURN (1)   0
 13  L13:   LOADA        0[SB]
 14         LOADL        1
 15         LOADA        8[CB]
 16         JUMP         L15
 17  L14:   HALT   (4)   
 18         PUSH         1
 19         LOADA        0[SB]
 20         LOADL        1
 21         CALL         newobj  
 22         STORE        3[LB]
 23         LOAD         3[LB]
 24         LOADL        0
 25         LOADL        44
 26         HALT   (4)   
 27         CALL         fieldupd
 28         LOAD         3[LB]
 29         CALL         L10
 30         CALL         putint  
 31         PUSH         1
 32         LOADA        3[SB]
 33         LOADL        2
 34         CALL         newobj  
 35         STORE        4[LB]
 36         LOAD         4[LB]
 37         LOADL        0
 38         LOADL        66
 39         CALL         fieldupd
 40         HALT   (4)   
 41         LOAD         4[LB]
 42         CALLD        0
 43         CALL         putint  
 44         RETURN (1)   0
 45  L15:   LOADL        -1
 46         LOADL        0
 47         LOADL        -1
 48         CALL         L14
 49         HALT   (0)   
