# SimpleCompiler
 编译课设（词法分析器+语法分析器+非常草率的语义分析）  
 （记得切branch）
 
## 部分展示
### （一）使用实例介绍
#### 1.词法定义：
（1）对代码的词法分析中，i一般指复数，而非标识符。  
（2）代码的标识符不要出现大写。  
#### 2.语法定义：
（1）本人定义的语法极其简单（太复杂的真的好难编），并没有编写for语句的语法。  
（2）代码的类名、方法名都要显示定义可视性修饰符。  
#### 3.使用实例说明：
（1）code1.txt是测试词法用的，基本涵盖了编写的所有词法规则，可以成功运行。但是语法运行失败（语法写的比较简单）。  
（2）code2是测试语法和语义用的，可以成功运行。  
（3）lexerGrammar1是本人自行定义的标识符文法（全小写字符即可通过）。  
（4）lexerGrammar2是异常检测用的，由于第四行为空行，运行时会抛异常。删去第四行后，可以用于测试NFA->DFA转换（我自行定义的标识符文法状态太少了）。  
（5）lexerGrammar3是左线性文法，正规式为(a|b)a(ba|a)\*|ε。  
（6）parseGrammar1是本人基于Java定义的语法（比较简单 规则见第1-3条）。  
（7）parseGrammar2是一个二义性语法样例，就是加减乘除二义性（会报非LR1文法异常）。  
（8）semanticGrammar1是比较基本的语义规则，在#之后即为语义规则，如果有print则输出四元式。  
#### 4.javadoc：
 可进入路径：\javadoc\index.html查看本项目的javadoc。  
#### 5.代码说明文档：
 该文档说明了本编译器的使用方法。
### （二）部分效果展示
#### 1.词法分析结果
![image](https://user-images.githubusercontent.com/61185595/121019651-38817580-c7d2-11eb-9b11-e115a2cc75e7.png)
#### 2.语法分析结果
##### (1)分析结果
![image](https://user-images.githubusercontent.com/61185595/121019723-47682800-c7d2-11eb-98e3-65123c0f6fc0.png)
##### (2)GOTO表
![image](https://user-images.githubusercontent.com/61185595/121019704-433c0a80-c7d2-11eb-8410-a3b7e3f208e7.png)
#### 3.语义分析结果
![image](https://user-images.githubusercontent.com/61185595/121019840-5fd84280-c7d2-11eb-9fe2-9b05832bac93.png)
