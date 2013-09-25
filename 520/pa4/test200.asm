  0         JUMP         L11
  1  L10:   LOADL        10001
  2         LOADL        10002
  3         LOADL        10003
  4         LOAD         -2[LB]
  5         HALT   (4)   
  6         CALL         add     
  7         RETURN (1)   0
  8  L11:   LOADL        100
  9         LOADL        101
 10         LOADL        102
 11         LOADL        103
 12         LOADL        104
 13         LOADL        105
 14         LOADL        106
 15         LOADL        107
 16         LOADL        108
 17         LOADL        109
 18         LOADL        -1
 19         HALT   (4)   
 20         LOADA        1[CB]
 21         HALT   (4)   
 22         CALLI  
 23         LOADL        65537
 24         LOADL        5
 25         CALL         newarr  
 26         LOADL        3
 27         CALL         newarr  
 28         LOAD         12[SB]
 29         LOADL        2
 30         LOADL        -12345
 31         CALL         arrayupd
 32         LOAD         12[SB]
 33         LOADL        2
 34         CALL         arrayref
 35         LOADA        6[HT]
 36         HALT   (4)   
 37         STOREI 
 38         LOADA        2[SB]
 39         LOADL        5
 40         CALL         newobj  
 41         LOADL        27599
 42         CALL         putint  
 43         HALT   (4)   
 44         HALT   (0)   
