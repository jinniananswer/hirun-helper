# IDEA 设置规范

#### 采用 4 个空格缩进，禁止使用 tab 字符

File -> Settings -> Editor -> Code Style -> Java -> Tabs and Indents
* 不勾选 Use tab character 选项
* Tab size: 4 
* Indent: 4

注意：JavaScript、CSS、HTML、JSP 等语言同上。


#### 显示方法分隔符，减轻眼疲劳。

File -> Settings -> Editor -> General -> Appearance
* 勾选 show method separators

#### 代码提示不区分大小写，非常方便。

File -> Settings -> Editor -> General -> Code Completed
* 不勾选 Match case

#### 设置文件头

File -> Settings -> Editor -> File and Code Templates -> Includes -> File Header
```java
/**
 *
 * @author Steven
 * @date ${YEAR}-${MONTH}-${DAY}
 */
```

#### File Encodings 设置为 UTF-8

File -> Settings -> Editor -> File Encodings
* Global Encodings: UTF-8
* Project Encodings: UTF-8
* Default encoding for properties files: UTF-8
* Create UTF-8 files: with NO BOM

#### 文件中的换行符使用 Unix 格式，不要使用 Windows 格式。
File -> Settings -> Code Style -> General
* Line separator: Unix and macOS(\n)

#### 设置 package 的展现方式。
Alt + 1 调出 Project 面板，点击右上角的齿轮图标，勾选 Compact Middle Package。