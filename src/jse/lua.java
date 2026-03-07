/*******************************************************************************
* Copyright (c) 2009-2012 Luaj.org. All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
******************************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaDebugger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Print;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.JsePlatform;

import org.luaj.vm2.decompiler.TestDecompiler;


/**
 * lua command for use in JSE environments.
 */
public class lua {
	private static final String version = Lua._VERSION + " Copyright (c) 2026 Nirithy";

	private static final String usage = 
		"usage: java -cp luaj-jse.jar lua [options] [script [args]].\n" +
		"Available options are:\n" +
		"  -e stat  execute string 'stat'\n" +
		"  -l name  require library 'name'\n" +
		"  -i       enter interactive mode after executing 'script'\n" +
		"  -v       show version information\n" +
		"  -b      	use luajc bytecode-to-bytecode compiler (requires bcel on class path)\n" +
		"  -n      	nodebug - do not load debug library by default\n" +
		"  -d      	enable debugger mode\n" +
		"  -D      	decompile mode (analyze bytecode)\n" +
		"  -p      	print the prototype\n" +
		"  -c enc  	use the supplied encoding 'enc' for input files\n" +
		"  --       stop handling options\n" +
		"  -        execute stdin and stop handling options";

	private static void usageExit() {
		System.out.println(usage);
		System.exit(-1);		
	}

	private static Globals globals;
	private static boolean print = false;
	private static String encoding = null;
	private static boolean debugger = false;
	private static boolean decompile = false;
	
	public static void main( String[] args ) throws IOException {

		// process args
		boolean interactive = (args.length == 0);
		boolean versioninfo = false;
		boolean processing = true;
		boolean nodebug = false;
		boolean luajc = false;
		Vector libs = null;
		try {
			// stateful argument processing
			for ( int i=0; i<args.length; i++ ) {
				if ( ! processing || ! args[i].startsWith("-") ) {
					// input file - defer to last stage
					break;
				} else if ( args[i].length() <= 1 ) {
					// input file - defer to last stage
					break;
				} else {
					switch ( args[i].charAt(1) ) {
					case 'e':
						if ( ++i >= args.length )
							usageExit();
						// input script - defer to last stage
						break;
					case 'b':
						luajc = true;
						break;
					case 'l':
						if ( ++i >= args.length )
							usageExit();
						libs = libs!=null? libs: new Vector();
						libs.addElement( args[i] );
						break;
					case 'i':
						interactive = true;
						break;
					case 'v':
						versioninfo = true;
						break;
					case 'n':
						nodebug = true;
						break;
					case 'd':
						debugger = true;
						break;
					case 'D':
						decompile = true;
						break;
					case 'p':
						print = true;
						break;
					case 'c':
						if ( ++i >= args.length )
							usageExit();
						encoding = args[i];
						break;
					case '-':
						if ( args[i].length() > 2 )
							usageExit();
						processing = false;
						break;
					default:
						usageExit();
						break;
					}
				}
			}

			// echo version
			if ( versioninfo )
				System.out.println(version);
			
			// new lua state
			globals = nodebug? JsePlatform.standardGlobals(): JsePlatform.debugGlobals();

			
			// 启用调试器
			if ( debugger ) {
				LuaDebugger dbg = LuaDebugger.getInstance();
				dbg.setGlobalEnv(globals);
				dbg.enable(true);
			}
			
			for ( int i=0, n=libs!=null? libs.size(): 0; i<n; i++ )
				loadLibrary( (String) libs.elementAt(i) );
			
			// input script processing
			processing = true;
			for ( int i=0; i<args.length; i++ ) {
				if ( ! processing || ! args[i].startsWith("-") ) {
					processScript( new FileInputStream(args[i]), args[i], args, i );
					break;
				} else if ( "-".equals( args[i] ) ) {
					processScript( System.in, "=stdin", args, i );
					break;
				} else {
					switch ( args[i].charAt(1) ) {
					case 'l':
					case 'c':
						++i;
						break;
					case 'e':
						++i;
						processScript( new ByteArrayInputStream(args[i].getBytes()), "string", args, i );
						break;
					case '-':
						processing = false;
						break;
					}
				}
			}
			
			if ( interactive )
				interactiveMode();
			
		} catch ( IOException ioe ) {
			System.err.println( ioe.toString() );
			System.exit(-2);
		}
	}

	private static void loadLibrary( String libname ) throws IOException {
		LuaValue slibname =LuaValue.valueOf(libname); 
		try {
			// load via plain require
			globals.get("require").call(slibname);
		} catch ( Exception e ) {
			try {
				// load as java class
				LuaValue v = (LuaValue) Class.forName(libname).newInstance(); 
				v.call(slibname, globals);
			} catch ( Exception f ) {
				throw new IOException("loadLibrary("+libname+") failed: "+e+","+f );
			}
		}
	}
	
	private static void processScript( InputStream script, String chunkname, String[] args, int firstarg ) throws IOException {
		try {
			LuaValue c;
			try {
				script = new BufferedInputStream(script);
				
				// 反编译模式
				if ( decompile ) {
					script.mark(Integer.MAX_VALUE);
					TestDecompiler.analyzeBytecode(script, chunkname);
					return;
				}
				
				c = encoding != null? 
						globals.load(new InputStreamReader(script, encoding), chunkname):
						globals.load(script, chunkname, "bt", globals);
			} finally {
				script.close();
			}
			if (print && c.isclosure())
				Print.print(c.checkclosure().p);
			Varargs scriptargs = setGlobalArg(chunkname, args, firstarg, globals);
			c.invoke( scriptargs );
		} catch ( Exception e ) {
			e.printStackTrace( System.err );
		}
	}

	private static Varargs setGlobalArg(String chunkname, String[] args, int i, LuaValue globals) {
		if (args == null)
			return LuaValue.NONE;
		LuaTable arg = LuaValue.tableOf();
		for ( int j=0; j<args.length; j++ )
			arg.set( j-i, LuaValue.valueOf(args[j]) );
		arg.set(0, LuaValue.valueOf(chunkname));
		arg.set(-1, LuaValue.valueOf("luaj"));
		globals.set("arg", arg);
		return arg.unpack();
	}

	private static void interactiveMode( ) throws IOException {
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
		
		System.out.println("\n========================================");
		System.out.println("  Luaj-JSE 交互模式");
		System.out.println("  版本: " + version);
		System.out.println("========================================");
		
		while ( true ) {
			System.out.println("\n可用命令:");
			System.out.println("  1. run <文件>       - 运行Lua脚本");
			System.out.println("  2. debug <文件>     - 调试模式运行");
			System.out.println("  3. decompile <文件> - 反编译字节码");
			System.out.println("  4. compile <文件>   - 编译Lua脚本");
			System.out.println("  5. eval <代码>      - 执行Lua代码");
			System.out.println("  6. help             - 显示帮助");
			System.out.println("  7. quit             - 退出");
			System.out.print("\n> ");
			System.out.flush();
			
			String line = reader.readLine();
			if ( line == null )
				return;
			
			line = line.trim();
			if (line.isEmpty()) continue;
			
			String[] parts = line.split("\\s+", 2);
			String cmd = parts[0].toLowerCase();
			String arg = parts.length > 1 ? parts[1] : "";
			
			// 支持数字序号
			switch (cmd) {
				case "1":
					cmd = "run";
					break;
				case "2":
					cmd = "debug";
					break;
				case "3":
					cmd = "decompile";
					break;
				case "4":
					cmd = "compile";
					break;
				case "5":
					cmd = "eval";
					break;
				case "6":
					cmd = "help";
					break;
				case "7":
					cmd = "quit";
					break;
			}
			
			try {
				switch (cmd) {
					case "quit":
					case "exit":
						System.out.println("再见!");
						return;
						
					case "help":
						System.out.println("\n帮助信息:");
						System.out.println("  run <文件>       - 运行指定的Lua脚本");
						System.out.println("  debug <文件>     - 以调试模式运行脚本");
						System.out.println("  decompile <文件> - 反编译Lua字节码文件");
						System.out.println("  compile <文件>   - 编译Lua脚本为字节码");
						System.out.println("  eval <代码>      - 直接执行Lua代码");
						System.out.println("  quit/exit        - 退出程序");
						System.out.println("\n提示: 可以使用数字序号(1-7)或命令名称");
						break;
						
					case "run":
						if (arg.isEmpty()) {
							System.out.print("请输入文件路径: ");
							arg = reader.readLine().trim();
						}
						if (!arg.isEmpty()) {
							debugger = false;
							decompile = false;
							print = false;
							processScript(new FileInputStream(arg), arg, null, 0);
						}
						break;
						
					case "debug":
						if (arg.isEmpty()) {
							System.out.print("请输入文件路径: ");
							arg = reader.readLine().trim();
						}
						if (!arg.isEmpty()) {
							debugger = true;
							decompile = false;
							print = false;
							LuaDebugger dbg = LuaDebugger.getInstance();
							dbg.setGlobalEnv(globals);
							dbg.enable(true);
							processScript(new FileInputStream(arg), arg, null, 0);
						}
						break;
						
					case "decompile":
					case "disasm":
						if (arg.isEmpty()) {
							System.out.print("请输入字节码文件路径: ");
							arg = reader.readLine().trim();
						}
						if (!arg.isEmpty()) {
							debugger = false;
							decompile = true;
							print = false;
							processScript(new FileInputStream(arg), arg, null, 0);
						}
						break;
						
					case "compile":
						if (arg.isEmpty()) {
							System.out.print("请输入文件路径: ");
							arg = reader.readLine().trim();
						}
						if (!arg.isEmpty()) {
							debugger = false;
							decompile = false;
							print = true;
							processScript(new FileInputStream(arg), arg, null, 0);
						}
						break;
						
					case "eval":
						if (arg.isEmpty()) {
							System.out.print("请输入Lua代码: ");
							arg = reader.readLine().trim();
						}
						if (!arg.isEmpty()) {
							debugger = false;
							decompile = false;
							print = false;
							processScript(new ByteArrayInputStream(arg.getBytes()), "=eval", null, 0);
						}
						break;
						
					default:
						// 尝试作为Lua代码执行
						debugger = false;
						decompile = false;
						print = false;
						processScript(new ByteArrayInputStream(line.getBytes()), "=stdin", null, 0);
						break;
				}
			} catch (Exception e) {
				System.out.println("错误: " + e.getMessage());
			}
		}
	}
}
