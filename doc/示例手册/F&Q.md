# 使用 .gitignore 的问题
> 用于定义不加入 Git 版本控制的文件和目录清单。
> 注：只能忽略那些原来没有被 track 的文件。如果某些文件已经被纳入了版本管理中，则修改 .gitignore 是无效的。 处理方法如下:
```shell
E:\idea-workspace\hi-tech\hirun-helper>git rm --cached .gradle -r
rm '.gradle/4.10.3/fileChanges/last-build.bin'
rm '.gradle/4.10.3/fileContent/fileContent.lock'
rm '.gradle/4.10.3/fileHashes/fileHashes.bin'
rm '.gradle/4.10.3/fileHashes/fileHashes.lock'
rm '.gradle/4.10.3/fileHashes/resourceHashesCache.bin'
rm '.gradle/4.10.3/gc.properties'
rm '.gradle/4.10.3/javaCompile/classAnalysis.bin'
rm '.gradle/4.10.3/javaCompile/jarAnalysis.bin'
rm '.gradle/4.10.3/javaCompile/javaCompile.lock'
rm '.gradle/4.10.3/javaCompile/taskHistory.bin'
rm '.gradle/4.10.3/taskHistory/taskHistory.bin'
rm '.gradle/4.10.3/taskHistory/taskHistory.lock'
rm '.gradle/buildOutputCleanup/buildOutputCleanup.lock'
rm '.gradle/buildOutputCleanup/cache.properties'
rm '.gradle/buildOutputCleanup/outputFiles.bin'
rm '.gradle/vcs-1/gc.properties'
```

# 使用 idea 的 gradle 插件运行 build 的问题
> build 会连带执行 Task: test，如果想要跳过 Task: test，可以定义 -x test
> 并通过 Run 'hirun-help [build]' with Coverage 来执行
```shell
Run/Debug Configurations:
-> Gradle:
-> -> hirun-helper [build]
-> -> -> Arguments: -x test
```