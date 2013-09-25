  0         JUMP         L17
  1  L10:   PUSH         1
  2         LOADL        1
  3         STORE        3[LB]
  4         LOADL        2
  5         LOAD         3[LB]
  6         CALL         mult    
  7         LOAD         3[LB]
  8         CALL         add     
  9         LOADL        1
 10         CALL         sub     
 11         STORE        3[LB]
 12         LOAD         3[LB]
 13         LOADL        2
 14         CALL         eq      
 15         JUMPIF (0)   L11
 16         LOADL        3
 17         STORE        3[LB]
 18         JUMP         L12
 19  L11:   LOADL        1
 20         CALL         neg     
 21         STORE        3[LB]
 22  L12:   PUSH         1
 23         LOADL        0
 24         STORE        4[LB]
 25         JUMP         L14
 26  L13:   LOAD         4[LB]
 27         LOADL        1
 28         CALL         add     
 29         STORE        4[LB]
 30         LOAD         4[LB]
 31         STORE        3[LB]
 32         POP          0
 33  L14:   LOAD         4[LB]
 34         LOADL        4
 35         CALL         lt      
 36         JUMPIF (1)   L13
 37         PUSH         1
 38         LOADA        3[SB]
 39         LOADL        2
 40         CALL         newobj  
 41         STORE        5[LB]
 42         LOADL        5
 43         LOAD         5[LB]
 44         LOADL        0
 45         CALL         fieldref
 46         CALL         add     
 47         STORE        3[LB]
 48         LOAD         5[LB]
 49         LOADL        1
 50         LOADA        8[SB]
 51         LOADL        2
 52         CALL         newobj  
 53         CALL         fieldupd
 54         LOAD         5[LB]
 55         LOADL        1
 56         CALL         fieldref
 57         LOADL        0
 58         LOADL        6
 59         CALL         fieldupd
 60         LOAD         5[LB]
 61         LOADL        1
 62         CALL         fieldref
 63         LOADL        0
 64         CALL         fieldref
 65         STORE        3[LB]
 66         LOAD         5[LB]
 67         LOADL        1
 68         CALL         fieldref
 69         LOADL        1
 70         LOAD         5[LB]
 71         CALL         fieldupd
 72         LOAD         5[LB]
 73         LOADL        1
 74         CALL         fieldref
 75         LOADL        1
 76         CALL         fieldref
 77         LOADL        0
 78         LOAD         3[LB]
 79         LOADL        1
 80         CALL         add     
 81         CALL         fieldupd
 82         LOAD         5[LB]
 83         LOADL        0
 84         CALL         fieldref
 85         STORE        3[LB]
 86         PUSH         1
 87         LOADL        4
 88         STORE        6[LB]
 89         PUSH         1
 90         LOAD         6[LB]
 91         CALL         newarr  
 92         STORE        7[LB]
 93         LOAD         6[LB]
 94         LOADL        2
 95         CALL         mult    
 96         STORE        3[LB]
 97         LOADL        1
 98         STORE        4[LB]
 99         LOAD         7[LB]
100         LOADL        0
101         LOAD         4[LB]
102         CALL         arrayupd
103         JUMP         L16
104  L15:   LOAD         7[LB]
105         LOAD         4[LB]
106         LOAD         7[LB]
107         LOAD         4[LB]
108         LOADL        1
109         CALL         sub     
110         CALL         arrayref
111         LOAD         4[LB]
112         CALL         add     
113         CALL         arrayupd
114         LOAD         4[LB]
115         LOADL        1
116         CALL         add     
117         STORE        4[LB]
118         POP          0
119  L16:   LOAD         4[LB]
120         LOAD         6[LB]
121         CALL         lt      
122         JUMPIF (1)   L15
123         LOAD         7[LB]
124         LOADL        3
125         CALL         arrayref
126         LOADL        2
127         CALL         add     
128         STORE        3[LB]
129         LOAD         5[LB]
130         CALL         L18
131         LOADL        999
132         CALL         putint  
133         CALL         puteol  
134         RETURN (0)   1
135  L17:   LOADL        -1
136         LOADL        1
137         LOADA        1[CB]
138         JUMP         L21
139  L18:   PUSH         1
140         LOADL        10
141         STORE        3[LB]
142         LOADA        0[OB]
143         LOADL        0
144         LOADL        11
145         CALL         fieldupd
146         LOAD         1[OB]
147         LOADL        1
148         CALL         fieldref
149         LOADL        0
150         CALL         fieldref
151         STORE        3[LB]
152         LOADA        0[OB]
153         LOADL        0
154         LOADL        4
155         CALL         fieldupd
156         LOADL        1
157         LOADL        3
158         LOADL        4
159         LOADA        0[OB]
160         CALL         L19
161         CALL         add     
162         STORE        3[LB]
163         LOADL        13
164         CALL         putint  
165         CALL         puteol  
166         LOADL        8
167         LOADL        3
168         LOAD         1[OB]
169         CALL         L22
170         CALL         add     
171         CALL         putint  
172         CALL         puteol  
173         LOADA        0[OB]
174         LOADL        0
175         LOADL        4
176         CALL         fieldupd
177         LOAD         1[OB]
178         LOADL        0
179         LOADL        5
180         CALL         fieldupd
181         LOADL        2
182         LOADA        0[OB]
183         LOADA        0[OB]
184         LOADL        1
185         CALL         fieldref
186         LOADA        0[OB]
187         CALL         L20
188         CALL         add     
189         CALL         putint  
190         CALL         puteol  
191         RETURN (0)   0
192  L19:   LOAD         0[OB]
193         LOAD         -2[LB]
194         CALL         add     
195         LOAD         -1[LB]
196         CALL         add     
197         RETURN (1)   2
198  L20:   LOAD         -2[LB]
199         LOADL        0
200         CALL         fieldref
201         LOAD         -1[LB]
202         LOADL        0
203         CALL         fieldref
204         CALL         add     
205         LOADA        0[OB]
206         LOADL        0
207         CALL         fieldref
208         CALL         add     
209         RETURN (1)   2
210  L21:   LOADL        -1
211         LOADL        3
212         LOADA        139[CB]
213         LOADA        192[CB]
214         LOADA        198[CB]
215         JUMP         L24
216  L22:   PUSH         1
217         LOADL        1
218         STORE        3[LB]
219         LOAD         -1[LB]
220         LOADL        1
221         CALL         gt      
222         JUMPIF (0)   L23
223         LOAD         -1[LB]
224         LOAD         -1[LB]
225         LOADL        1
226         CALL         sub     
227         LOADA        0[OB]
228         CALL         L22
229         CALL         mult    
230         STORE        3[LB]
231  L23:   LOAD         3[LB]
232         RETURN (1)   1
233  L24:   LOADL        -1
234         LOADL        1
235         LOADA        216[CB]
236         LOADL        -1
237         CALL         L10
238         HALT   (4)   
239         HALT   (0)   
