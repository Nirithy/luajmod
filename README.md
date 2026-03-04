# LuajMod - 增强版 LuaJ 解释器

这是一个基于 [Luaj](https://github.com/luaj/luaj) 的增强版本，添加了以下功能：

## 新增功能

### 1. LuaDebugger - 完整调试器
- 断点设置和管理
- 单步执行（step into/step over）
- 变量监视
- 函数替换
- 调用栈跟踪

### 2. GgLib - GameGuardian模拟库
- `gg.choice()` - 单选菜单
- `gg.multiChoice()` - 多选菜单
- `gg.prompt()` - 输入对话框
- `gg.alert()` - 提示框
- `gg.toast()` - Toast提示
- `gg.showUiButton()` / `gg.isClickedUiButton()` - 悬浮按钮
- `gg.searchNumber()` / `gg.searchPointer()` - 内存搜索
- `gg.getValues()` / `gg.getValuesRange()` - 获取内存值
- `gg.addList()` / `gg.editAll()` - 列表编辑
- `gg.setValues()` - 批量修改内存
- `gg.setVisible()` - 窗口可见性
- `gg.getFile()` / `gg.saveFile()` - 文件操作

### 3. 反编译器 - 字节码分析工具
- 反汇编所有指令
- 伪执行分析
- 字符串捕获
- 控制流图(CFG)构建
- 交互式命令模式
- 函数列表和详情查看
- 导出分析结果

### 4. Lua 5.3+ Opcode支持
- `OP_IDIV` - 整数除法
- `OP_BNOT` - 位取反
- `OP_BAND` - 位与
- `OP_BOR` - 位或
- `OP_BXOR` - 位异或
- `OP_SHL` - 左移
- `OP_SHR` - 右移
- `OP_GETFIELDU` - 从upvalue获取字段
- `OP_GETFIELDT` - 从stack获取字段
- `OP_SETFIELDU` / `OP_SETFIELDT` - 设置字段
- `OP_NEWTABLE0` - 创建空表

### 5. 交互模式增强
- 支持数字序号(1-7)和命令名称两种方式
- 友好的提示信息
- 完整的命令帮助系统

### 6. 其他改进
- 详细错误信息（显示调用目标、类型、建议）
- GG库横幅只显示一次
- JAR文件包含Main-Class清单
- `.gitignore`配置

## 快速开始

### 交互模式
```bash
# 直接运行进入交互模式
java -jar luaj-jse-3.0.2.jar
```

交互模式命令：
| 序号 | 命令 | 说明 |
|------|------|------|
| 1 | `run <文件>` | 运行Lua脚本 |
| 2 | `debug <文件>` | 调试模式运行 |
| 3 | `decompile <文件>` | 反编译字节码 |
| 4 | `compile <文件>` | 编译Lua脚本 |
| 5 | `eval <代码>` | 执行Lua代码 |
| 6 | `help` | 显示帮助 |
| 7 | `quit` | 退出 |

### 命令行模式
```bash
# 运行脚本
java -jar luaj-jse-3.0.2.jar script.lua

# 调试模式
java -jar luaj-jse-3.0.2.jar -d script.lua

# 反编译模式
java -jar luaj-jse-3.0.2.jar -D script.luac

# 编译脚本
java -jar luaj-jse-3.0.2.jar -p script.lua

# 显示版本
java -jar luaj-jse-3.0.2.jar -v
```

### 反编译器交互命令
```bash
# 反编译模式交互命令
list              - 列出所有函数
info <id>         - 显示函数详情
run <id>          - 伪执行指定函数
runall            - 伪执行所有函数
exec <id>         - 真执行指定函数
execall           - 真执行所有函数
disasm <id>       - 反汇编指定函数
strings           - 显示捕获的字符串
funcs             - 显示已添加的用户函数
env               - 显示环境变量和已补全的表/方法
add <名称>        - 添加用户函数
edit <名称>       - 编辑/替换环境变量或函数
del <名称>        - 删除用户函数
analyze           - 完整分析(反汇编+伪执行)
export <文件>     - 导出完整分析到文件
quit              - 退出
```

## 示例

### 运行Lua脚本
```bash
java -jar luaj-jse-3.0.2.jar script.lua
```

### 调试模式
```bash
java -jar luaj-jse-3.0.2.jar -d script.lua
```

### 反编译字节码
```bash
java -jar luaj-jse-3.0.2.jar -D script.luac
```

### 在Java中使用
```java
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

Globals globals = JsePlatform.standardGlobals();
LuaValue chunk = globals.load("print('hello, world')");
chunk.call();
```

### 使用GameGuardian模拟库
```lua
-- 显示提示
gg.alert("操作完成!")

-- 显示菜单
local choice = gg.choice({ok="确定", cancel="取消"}, nil, "请选择")

-- 输入对话框
local result = gg.prompt({"用户名", "密码"}, {"", ""}, {"text", "number"})

-- 搜索内存
local values = gg.searchNumber("12345", gg.TYPE_DWORD, false, gg.SIGN_EQUAL, 0, gg.SIGN_EQUAL)

-- 获取内存值
local value = gg.getValues(0x12345678, gg.TYPE_DWORD, 1)

-- 设置内存值
gg.setValues({{address = 0x12345678, flags = gg.TYPE_DWORD, value = 9999}})
```

### 调试器使用
```bash
# 启动调试模式
java -jar luaj-jse-3.0.2.jar -d script.lua

# 调试命令（在脚本中使用）
debugger.setBreakpoint("script.lua", 10)  -- 在第10行设置断点
debugger.stepInto()  -- 单步进入
debugger.stepOver()  -- 单步跳过
debugger.continue()  -- 继续执行
debugger.replaceFunction("funcName", newFunc)  -- 替换函数
```

## 项目结构

```
LuajMod/
├── src/
│   ├── core/org/luaj/vm2/        # 核心虚拟机
│   │   ├── LuaClosure.java          # 增强的虚拟机执行
│   │   ├── LuaDebugger.java        # 调试器实现
│   │   └── StringInterceptor.java   # 字符串拦截器
│   └── jse/org/luaj/vm2/
│       ├── lua.java              # 主入口（交互模式）
│       ├── luajc.java            # 编译器
│       ├── lib/jse/
│       │   ├── GgLib.java          # GameGuardian模拟库
│       │   └── ...
│       └── decompiler/          # 反编译器
│           ├── TestDecompiler.java  # 反编译器主类
│           ├── Disassembler.java    # 反汇编器
│           ├── PseudoExecution.java  # 伪执行引擎
│           └── StringCapture.java  # 字符串捕获
├── gg/                          # GameGuardian源码参考
└── build.xml                     # 构建配置
```

## 许可证

本项目基于 [Luaj](https://github.com/luaj/luaj) 开发，遵循其原始许可协议。

## 贡献

欢迎提交 Issue 和 Pull Request！

## 版本历史

### 3.0.2 (2026-03-04)
- 添加 LuaDebugger 完整调试器
- 添加 GgLib GameGuardian模拟库
- 添加字节码反编译器
- 添加 Lua 5.3+ Opcode支持
- 添加交互模式（数字序号+命令名称）
- 改进错误信息显示
