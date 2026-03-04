/*******************************************************************************
* Copyright (c) 2009 Luaj.org. All rights reserved.
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
package org.luaj.vm2;

import org.luaj.vm2.lib.DebugLib.CallFrame;

/**
 * Extension of {@link LuaFunction} which executes lua bytecode.
 * <p>
 * A {@link LuaClosure} is a combination of a {@link Prototype}
 * and a {@link LuaValue} to use as an environment for execution.
 * Normally the {@link LuaValue} is a {@link Globals} in which case the environment
 * will contain standard lua libraries.
 * 
 * <p>
 * There are three main ways {@link LuaClosure} instances are created:
 * <ul>
 * <li>Construct an instance using {@link #LuaClosure(Prototype, LuaValue)}</li>
 * <li>Construct it indirectly by loading a chunk via {@link Globals#load(java.io.Reader, String)}
 * <li>Execute the lua bytecode {@link Lua#OP_CLOSURE} as part of bytecode processing
 * </ul>
 * <p>
 * To construct it directly, the {@link Prototype} is typically created via a compiler such as
 * {@link org.luaj.vm2.compiler.LuaC}:
 * <pre> {@code
 * String script = "print( 'hello, world' )";
 * InputStream is = new ByteArrayInputStream(script.getBytes());
 * Prototype p = LuaC.instance.compile(is, "script");
 * LuaValue globals = JsePlatform.standardGlobals();
 * LuaClosure f = new LuaClosure(p, globals);
 * f.call();
 * }</pre>
 * <p>
 * To construct it indirectly, the {@link Globals#load(java.io.Reader, String)} method may be used:
 * <pre> {@code
 * Globals globals = JsePlatform.standardGlobals();
 * LuaFunction f = globals.load(new StringReader(script), "script");
 * LuaClosure c = f.checkclosure();  // This may fail if LuaJC is installed.
 * c.call();
 * }</pre>
 * <p>
 * In this example, the "checkclosure()" may fail if direct lua-to-java-bytecode
 * compiling using LuaJC is installed, because no LuaClosure is created in that case
 * and the value returned is a {@link LuaFunction} but not a {@link LuaClosure}.
 * <p>
 * Since a {@link LuaClosure} is a {@link LuaFunction} which is a {@link LuaValue},
 * all the value operations can be used directly such as:
 * <ul>
 * <li>{@link LuaValue#call()}</li>
 * <li>{@link LuaValue#call(LuaValue)}</li>
 * <li>{@link LuaValue#invoke()}</li>
 * <li>{@link LuaValue#invoke(Varargs)}</li>
 * <li>{@link LuaValue#method(String)}</li>
 * <li>{@link LuaValue#method(String,LuaValue)}</li>
 * <li>{@link LuaValue#invokemethod(String)}</li>
 * <li>{@link LuaValue#invokemethod(String,Varargs)}</li>
 * <li> ...</li>
 * </ul>
 * @see LuaValue
 * @see LuaFunction
 * @see LuaValue#isclosure()
 * @see LuaValue#checkclosure()
 * @see LuaValue#optclosure(LuaClosure)
 * @see LoadState
 * @see Globals#compiler
 */
public class LuaClosure extends LuaFunction {
	private static final UpValue[] NOUPVALUES = new UpValue[0];
	
	public final Prototype p;

	public UpValue[] upValues;
	
	final Globals globals;
	
	/** Create a closure around a Prototype with a specific environment.
	 * If the prototype has upvalues, the environment will be written into the first upvalue.
	 * @param p the Prototype to construct this Closure for.
	 * @param env the environment to associate with the closure.
	 */
	public LuaClosure(Prototype p, LuaValue env) {
		this.p = p;
		this.initupvalue1(env);
		globals = env instanceof Globals? (Globals) env: null;
	}
	
	public void initupvalue1(LuaValue env) {
		if (p.upvalues == null || p.upvalues.length == 0)
			this.upValues = NOUPVALUES;
		else {
			this.upValues = new UpValue[p.upvalues.length];
			this.upValues[0] = new UpValue(new LuaValue[] {env}, 0);
		}
	}

	
	public boolean isclosure() {
		return true;
	}
	
	public LuaClosure optclosure(LuaClosure defval) {
		return this;
	}

	public LuaClosure checkclosure() {
		return this;
	}
	
	public String tojstring() {
		return "function: " + p.toString();
	}
	
	private LuaValue[] getNewStack() {
		int max = p.maxstacksize;
		LuaValue[] stack = new LuaValue[max];
		System.arraycopy(NILS, 0, stack, 0, max);
		return stack;
	}
	
	public final LuaValue call() {
		LuaValue[] stack = getNewStack();
		return execute(stack,NONE).arg1();
	}

	public final LuaValue call(LuaValue arg) {
		LuaValue[] stack = getNewStack();
		switch ( p.numparams ) {
		default: stack[0]=arg; return execute(stack,NONE).arg1();
		case 0: return execute(stack,arg).arg1();
		}
	}
	
	public final LuaValue call(LuaValue arg1, LuaValue arg2) {
		LuaValue[] stack = getNewStack();
		switch ( p.numparams ) {
		default: stack[0]=arg1; stack[1]=arg2; return execute(stack,NONE).arg1();
		case 1: stack[0]=arg1; return execute(stack,arg2).arg1();
		case 0: return execute(stack,p.is_vararg!=0? varargsOf(arg1,arg2): NONE).arg1();
		}
	}

	public final LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		LuaValue[] stack = getNewStack();
		switch ( p.numparams ) {
		default: stack[0]=arg1; stack[1]=arg2; stack[2]=arg3; return execute(stack,NONE).arg1();
		case 2: stack[0]=arg1; stack[1]=arg2; return execute(stack,arg3).arg1();
		case 1: stack[0]=arg1; return execute(stack,p.is_vararg!=0? varargsOf(arg2,arg3): NONE).arg1();
		case 0: return execute(stack,p.is_vararg!=0? varargsOf(arg1,arg2,arg3): NONE).arg1();
		}
	}

	public final Varargs invoke(Varargs varargs) {
		return onInvoke(varargs).eval();
	}
	
	public final Varargs onInvoke(Varargs varargs) {
		LuaValue[] stack = getNewStack();
		for ( int i=0; i<p.numparams; i++ )
			stack[i] = varargs.arg(i+1);
		return execute(stack,p.is_vararg!=0? varargs.subargs(p.numparams+1): NONE);
	}
	
	protected Varargs execute( LuaValue[] stack, Varargs varargs ) {
		// loop through instructions
		int i,a,b,c,pc=0,top=0;
		LuaValue o;
		Varargs v = NONE;
		int[] code = p.code;
		LuaValue[] k = p.k;
		
		// upvalues are only possible when closures create closures
		// TODO: use linked list.
		UpValue[] openups = p.p.length>0? new UpValue[stack.length]: null;
		
		// allow for debug hooks
		if (globals != null && globals.debuglib != null)
			globals.debuglib.onCall( this, varargs, stack );

		// 调试器集成
		LuaDebugger debugger = LuaDebugger.getInstance();
		if (debugger.isEnabled()) {
			debugger.onFunctionCall(this, varargs);
		}

		// process instructions
		try {
			for (; true; ++pc) {
				if (globals != null && globals.debuglib != null)
					globals.debuglib.onInstruction( pc, v, top );
				
				// 调试器行检查
				if (debugger.isEnabled() && p.lineinfo != null && pc < p.lineinfo.length) {
					debugger.onLineChange(this, p.lineinfo[pc]);
				}
				
				// pull out instruction
				i = code[pc];
				a = ((i>>6) & 0xff);
				
				// process the op code
				switch ( i & 0x3f ) {
				
				case Lua.OP_MOVE:/*	A B	R(A):= R(B)					*/
					stack[a] = stack[i>>>23];
					continue;
					
				case Lua.OP_LOADK:/*	A Bx	R(A):= Kst(Bx)					*/
					stack[a] = k[i>>>14];
					continue;
					
				case Lua.OP_LOADKX:/*	A 	R(A) := Kst(extra arg)					*/
					++pc;
					i = code[pc];
					if ((i & 0x3f) != Lua.OP_EXTRAARG) {
						int op = i & 0x3f;
						throw new LuaError("OP_EXTRAARG expected after OP_LOADKX, got " +
							(op < Print.OPNAMES.length - 1 ? Print.OPNAMES[op] : "UNKNOWN_OP_" + op));
					}
					stack[a] = k[i>>>6];
					continue;
					
				case Lua.OP_LOADBOOL:/*	A B C	R(A):= (Bool)B: if (C) pc++			*/
	                stack[a] = (i>>>23!=0)? LuaValue.TRUE: LuaValue.FALSE;
	                if ((i&(0x1ff<<14)) != 0)
	                    ++pc; /* skip next instruction (if C) */
	                continue;
	
				case Lua.OP_LOADNIL: /*	A B	R(A):= ...:= R(A+B):= nil			*/
					for ( b=i>>>23; b-->=0; )
						stack[a++] = LuaValue.NIL;
					continue;
					
				case Lua.OP_GETUPVAL: /*	A B	R(A):= UpValue[B]				*/
	                stack[a] = upValues[i>>>23].getValue();
	                continue;
					
				case Lua.OP_GETTABUP: /*	A B C	R(A) := UpValue[B][RK(C)]			*/
					stack[a] = upValues[i>>>23].getValue().get((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
	                
				case Lua.OP_GETTABLE: /*	A B C	R(A):= R(B)[RK(C)]				*/
	                stack[a] = stack[i>>>23].get((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_SETTABUP: /*	A B C	UpValue[A][RK(B)] := RK(C)			*/
	                upValues[a].getValue().set(((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]), (c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_SETUPVAL: /*	A B	UpValue[B]:= R(A)				*/
					upValues[i>>>23].setValue(stack[a]);
					continue;
					
				case Lua.OP_SETTABLE: /*	A B C	R(A)[RK(B)]:= RK(C)				*/
					stack[a].set(((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]), (c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_NEWTABLE: /*	A B C	R(A):= {} (size = B,C)				*/
					stack[a] = new LuaTable(i>>>23,(i>>14)&0x1ff);
					continue;
					
				case Lua.OP_SELF: /*	A B C	R(A+1):= R(B): R(A):= R(B)[RK(C)]		*/
					stack[a+1] = (o = stack[i>>>23]);
					stack[a] = o.get((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_ADD: /*	A B C	R(A):= RK(B) + RK(C)				*/
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).add((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_SUB: /*	A B C	R(A):= RK(B) - RK(C)				*/
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).sub((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_MUL: /*	A B C	R(A):= RK(B) * RK(C)				*/
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).mul((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_DIV: /*	A B C	R(A):= RK(B) / RK(C)				*/
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).div((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_MOD: /*	A B C	R(A):= RK(B) % RK(C)				*/
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).mod((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_POW: /*	A B C	R(A):= RK(B) ^ RK(C)				*/
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).pow((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;
					
				case Lua.OP_UNM: /*	A B	R(A):= -R(B)					*/
					stack[a] = stack[i>>>23].neg();
					continue;
					
				case Lua.OP_NOT: /*	A B	R(A):= not R(B)				*/
					stack[a] = stack[i>>>23].not();
					continue;
					
				case Lua.OP_LEN: /*	A B	R(A):= length of R(B)				*/
					stack[a] = stack[i>>>23].len();
					continue;
					
				case Lua.OP_CONCAT: /*	A B C	R(A):= R(B).. ... ..R(C)			*/
					b = i>>>23;
					c = (i>>14)&0x1ff;
					{
						if ( c > b+1 ) {
							Buffer sb = stack[c].buffer();
							while ( --c>=b )
								sb.concatTo(stack[c]);
							stack[a] = sb.value();
						} else {
							stack[a] = stack[c-1].concat(stack[c]);
						}
					}
					continue;
					
				case Lua.OP_JMP: /*	A sBx	pc+=sBx; if (A) close all upvalues >= R(A - 1)	*/
					pc  += (i>>>14)-0x1ffff;
					if (a > 0) {
						for (--a, b = openups.length; --b>=0; )
							if (openups[b] != null && openups[b].index >= a) {
								openups[b].close();
								openups[b] = null;
							}
					}
					continue;
					
				case Lua.OP_EQ: /*	A B C	if ((RK(B) == RK(C)) ~= A) then pc++		*/
					if ( ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).eq_b((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]) != (a!=0) )
						++pc;
					continue;
					
				case Lua.OP_LT: /*	A B C	if ((RK(B) <  RK(C)) ~= A) then pc++  		*/
					if ( ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).lt_b((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]) != (a!=0) )
						++pc;
					continue;
					
				case Lua.OP_LE: /*	A B C	if ((RK(B) <= RK(C)) ~= A) then pc++  		*/
					if ( ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).lteq_b((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]) != (a!=0) )
						++pc;
					continue;
					
				case Lua.OP_TEST: /*	A C	if not (R(A) <=> C) then pc++			*/
					if ( stack[a].toboolean() != ((i&(0x1ff<<14))!=0) )
						++pc;
					continue;
					
				case Lua.OP_TESTSET: /*	A B C	if (R(B) <=> C) then R(A):= R(B) else pc++	*/
					/* note: doc appears to be reversed */
					if ( (o=stack[i>>>23]).toboolean() != ((i&(0x1ff<<14))!=0) )
						++pc;
					else
						stack[a] = o; // TODO: should be sBx?
					continue;
					
				case Lua.OP_CALL: /*	A B C	R(A), ... ,R(A+C-2):= R(A)(R(A+1), ... ,R(A+B-1)) */
					{
						int call_a = a;  // 保存a的值
						try {
							switch ( i & (Lua.MASK_B | Lua.MASK_C) ) {
							case (1<<Lua.POS_B) | (0<<Lua.POS_C): v=stack[call_a].invoke(NONE); top=call_a+v.narg(); continue;
							case (2<<Lua.POS_B) | (0<<Lua.POS_C): v=stack[call_a].invoke(stack[call_a+1]); top=call_a+v.narg(); continue;
							case (1<<Lua.POS_B) | (1<<Lua.POS_C): stack[call_a].call(); continue;
							case (2<<Lua.POS_B) | (1<<Lua.POS_C): stack[call_a].call(stack[call_a+1]); continue;
							case (3<<Lua.POS_B) | (1<<Lua.POS_C): stack[call_a].call(stack[call_a+1],stack[call_a+2]); continue;
							case (4<<Lua.POS_B) | (1<<Lua.POS_C): stack[call_a].call(stack[call_a+1],stack[call_a+2],stack[call_a+3]); continue;
							case (1<<Lua.POS_B) | (2<<Lua.POS_C): stack[call_a] = stack[call_a].call(); continue;
							case (2<<Lua.POS_B) | (2<<Lua.POS_C): stack[call_a] = stack[call_a].call(stack[call_a+1]); continue;
							case (3<<Lua.POS_B) | (2<<Lua.POS_C): stack[call_a] = stack[call_a].call(stack[call_a+1],stack[call_a+2]); continue;
                            case (4<<Lua.POS_B) | (2<<Lua.POS_C): stack[call_a] = stack[call_a].call(stack[call_a+1],stack[call_a+2],stack[call_a+3]); continue;
							default:
								b = i>>>23;
								c = (i>>14)&0x1ff;
								v = stack[call_a].invoke(b>0?
									varargsOf(stack, call_a+1, b-1): // exact arg count
									varargsOf(stack, call_a+1, top-v.narg()-(call_a+1), v));  // from prev top
								if ( c > 0 ) {
									v.copyto(stack, call_a, c-1);
									v = NONE;
								} else {
									top = call_a + v.narg();
									v = v.dealias();
								}
								continue;
							}
						} catch (LuaError e) {
							String funcInfo = stack[call_a].tojstring();
							if (funcInfo.length() > 80) funcInfo = funcInfo.substring(0, 80) + "...";
							throw new LuaError(e.getMessage() + "\n  [调用目标] " + funcInfo);
						}
					}
					
				case Lua.OP_TAILCALL: /*	A B C	return R(A)(R(A+1), ... ,R(A+B-1))		*/
					switch ( i & Lua.MASK_B ) {
					case (1<<Lua.POS_B): return new TailcallVarargs(stack[a], NONE);
					case (2<<Lua.POS_B): return new TailcallVarargs(stack[a], stack[a+1]);
					case (3<<Lua.POS_B): return new TailcallVarargs(stack[a], varargsOf(stack[a+1],stack[a+2]));
					case (4<<Lua.POS_B): return new TailcallVarargs(stack[a], varargsOf(stack[a+1],stack[a+2],stack[a+3]));
					default:
						b = i>>>23;
						v = b>0?
							varargsOf(stack,a+1,b-1): // exact arg count
							varargsOf(stack, a+1, top-v.narg()-(a+1), v); // from prev top
						return new TailcallVarargs( stack[a], v );
					}
					
				case Lua.OP_RETURN: /*	A B	return R(A), ... ,R(A+B-2)	(see note)	*/
					b = i>>>23;
					switch ( b ) {
					case 0: return varargsOf(stack, a, top-v.narg()-a, v);
					case 1: return NONE;
					case 2: return stack[a];
					default:
						return varargsOf(stack, a, b-1);
					}
					
				case Lua.OP_FORLOOP: /*	A sBx	R(A)+=R(A+2): if R(A) <?= R(A+1) then { pc+=sBx: R(A+3)=R(A) }*/
					{
			            LuaValue limit = stack[a + 1];
						LuaValue step  = stack[a + 2];
						LuaValue idx   = stack[a].add(step);
			            if (step.gt_b(0)? idx.lteq_b(limit): idx.gteq_b(limit)) {
		                    stack[a] = idx;
		                    stack[a + 3] = idx;
		                    pc += (i>>>14)-0x1ffff;
			            }
					}
					continue;
					
				case Lua.OP_FORPREP: /*	A sBx	R(A)-=R(A+2): pc+=sBx				*/
					{
						LuaValue init  = stack[a].checknumber("'for' initial value must be a number");
						LuaValue limit = stack[a + 1].checknumber("'for' limit must be a number");
						LuaValue step  = stack[a + 2].checknumber("'for' step must be a number");
						stack[a] = init.sub(step);
						stack[a + 1] = limit;
						stack[a + 2] = step;
						pc += (i>>>14)-0x1ffff;
					}
					continue;

				case Lua.OP_TFORCALL: /* A C	R(A+3), ... ,R(A+2+C) := R(A)(R(A+1), R(A+2));	*/
					v = stack[a].invoke(varargsOf(stack[a+1],stack[a+2]));
					c = (i>>14) & 0x1ff;
					while (--c >= 0)
						stack[a+3+c] = v.arg(c+1);
					v = NONE;
					continue;

				case Lua.OP_TFORLOOP: /* A sBx	if R(A+1) ~= nil then { R(A)=R(A+1); pc += sBx */
					if (!stack[a+1].isnil()) { /* continue loop? */
						stack[a] = stack[a+1];  /* save control varible. */
						pc += (i>>>14)-0x1ffff;
					}
					continue;
					
				case Lua.OP_SETLIST: /*	A B C	R(A)[(C-1)*FPF+i]:= R(A+i), 1 <= i <= B	*/
					{
		                if ( (c=(i>>14)&0x1ff) == 0 )
		                    c = code[++pc];
		                int offset = (c-1) * Lua.LFIELDS_PER_FLUSH;
		                o = stack[a];
		                if ( (b=i>>>23) == 0 ) {
		                    b = top - a - 1;
		                    int m = b - v.narg();
		                	int j=1;
		                	for ( ;j<=m; j++ )
		                    	o.set(offset+j, stack[a + j]);
		                	for ( ;j<=b; j++ )
		                    	o.set(offset+j, v.arg(j-m));
		                } else {
		                    o.presize( offset + b );
		                    for (int j=1; j<=b; j++)
		                    	o.set(offset+j, stack[a + j]);
		                }
					}
					continue;
					
				case Lua.OP_CLOSURE: /*	A Bx	R(A):= closure(KPROTO[Bx])	*/
					{
						Prototype newp = p.p[i>>>14];
						LuaClosure ncl = new LuaClosure(newp, globals);
						Upvaldesc[] uv = newp.upvalues;
						for ( int j=0, nup=uv.length; j<nup; ++j ) {
							if (uv[j].instack)  /* upvalue refes to local variable? */
								ncl.upValues[j] = findupval(stack, uv[j].idx, openups);
							else  /* get upvalue from enclosing function */
								ncl.upValues[j] = upValues[uv[j].idx];
						}
						stack[a] = ncl;
					}
					continue;
					
				case Lua.OP_VARARG: /*	A B	R(A), R(A+1), ..., R(A+B-1) = vararg		*/
					b = i>>>23;
					if ( b == 0 ) {
						top = a + (b = varargs.narg());
						v = varargs;
					} else {
						for ( int j=1; j<b; ++j )
							stack[a+j-1] = varargs.arg(j);
					}
					continue;

				case Lua.OP_EXTRAARG:
					throw new java.lang.IllegalArgumentException("Uexecutable opcode: OP_EXTRAARG");

				case Lua.OP_IDIV:
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).idiv((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;

				case Lua.OP_BNOT:
					stack[a] = stack[i>>>23].bnot();
					continue;

				case Lua.OP_BAND:
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).band((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;

				case Lua.OP_BOR:
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).bor((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;

				case Lua.OP_BXOR:
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).bxor((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;

				case Lua.OP_SHL:
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).shl((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;

				case Lua.OP_SHR:
					stack[a] = ((b=i>>>23)>0xff? k[b&0x0ff]: stack[b]).shr((c=(i>>14)&0x1ff)>0xff? k[c&0x0ff]: stack[c]);
					continue;

				case Lua.OP_GETFIELDU:
					// 从upvalue获取字段
					b = i >>> 23;
					c = (i >> 14) & 0x1ff;
					if (b >= upValues.length) {
						throw new LuaError("upvalue index out of range: " + b);
					}
					stack[a] = upValues[b].getValue().get((c<=0xff ? stack[c] : k[c&0x0ff]));
					continue;

				case Lua.OP_GETFIELDT:
					// 从stack获取字段 (opcode 48 = 0x30)
					c = (i >> 14) & 0x1ff;
					b = i >>> 23;
					{
						System.out.println("[DEBUG] OP_GETFIELDT: PC=" + pc + ", a=" + a + ", b=" + b + ", c=" + c + " (0x" + Integer.toHexString(c) + ")");
						System.out.println("[DEBUG]   c > 0xFF: " + (c > 0xff) + ", c & 0xFF: " + (c & 0xff));
						System.out.println("[DEBUG]   k.length: " + k.length);
						
						LuaValue table = stack[b];
						LuaValue key;
						if (c <= 0xff) {
							// c是寄存器索引
							key = stack[c];
							System.out.println("[DEBUG]   从寄存器R[" + c + "]获取key");
						} else {
							// c > 0xFF 表示常量索引
							// 检查下一条指令是否是EXTRAARG
							int nextInstr = code[pc];
							int nextOpcode = nextInstr & 0x3f;
							System.out.println("[DEBUG]   下一条指令: 0x" + Integer.toHexString(nextInstr) + ", opcode=" + nextOpcode);
							
							if (nextOpcode == Lua.OP_EXTRAARG) {
								// 从EXTRAARG获取真实索引
								int extraIdx = nextInstr >>> 6;
								System.out.println("[DEBUG]   从EXTRAARG获取索引: " + extraIdx);
								if (extraIdx >= k.length) {
									throw new LuaError("OP_GETFIELDT: EXTRAARG索引越界 extraIdx=" + extraIdx + ", k.length=" + k.length);
								}
								key = k[extraIdx];
								pc++; // 跳过EXTRAARG指令
							} else {
								// 没有EXTRAARG，使用 c & 0xFF 作为索引
								int kidx = c & 0xff;
								System.out.println("[DEBUG]   没有EXTRAARG, 使用kidx=" + kidx);
								if (kidx >= k.length) {
									throw new LuaError("OP_GETFIELDT: 常量索引越界 kidx=" + kidx + ", k.length=" + k.length + "\n" +
										"  这可能是因为字节码文件使用了不兼容的Lua版本编译\n" +
										"  请尝试用当前版本的Lua重新编译脚本");
								}
								key = k[kidx];
							}
						}
						System.out.println("[DEBUG]   table类型: " + table.typename() + ", key: " + key.tojstring());
						stack[a] = table.get(key);
					}
					continue;

				case Lua.OP_SETFIELDU:
					b = i >>> 23;
					c = (i >> 14) & 0x1ff;
					stack[b].set((c>0xff ? k[c&0x0ff] : stack[c]), stack[a]);
					continue;

				case Lua.OP_SETFIELDT:
					b = i >>> 23;
					c = (i >> 14) & 0x1ff;
					{
						LuaValue tbl = stack[b];
						LuaValue key = (c > 0xff ? k[c&0x0ff] : stack[c]);
						if (tbl.metatag(LuaValue.NEWINDEX).isfunction()) {
							tbl.metatag(LuaValue.NEWINDEX).call(tbl, key, stack[a]);
						} else {
							tbl.set(key, stack[a]);
						}
					}
					continue;

				case Lua.OP_NEWTABLE0:
					stack[a] = new LuaTable();
					continue;

				default:
					throw new java.lang.IllegalArgumentException("Illegal opcode: " + (i & 0x3f) + " (PC: " + pc + ")");
				}
			}
		} catch ( LuaError le ) {
			if (le.traceback == null)
				processErrorHooks(le, p, pc);
			throw le;
		} catch ( ArrayIndexOutOfBoundsException e ) {
			LuaError le = new LuaError("数组越界: " + e.getMessage() + 
				"\n  [PC] " + pc + 
				"\n  [指令] " + Integer.toHexString(code[pc-1]) +
				"\n  [opcode] " + (code[pc-1] & 0x3f) +
				"\n  [k.length] " + (k != null ? k.length : "null") +
				"\n  [stack.length] " + stack.length);
			processErrorHooks(le, p, pc);
			throw le;
		} catch ( Exception e ) {
			LuaError le = new LuaError(e);
			processErrorHooks(le, p, pc);
			throw le;
		} finally {
			if ( openups != null )
				for ( int u=openups.length; --u>=0; )
					if ( openups[u] != null )
						openups[u].close();
			if (globals != null && globals.debuglib != null)
				globals.debuglib.onReturn();
		}
	}

	/**
	 *  Run the error hook if there is one
	 *  @param msg the message to use in error hook processing.
	 * */
	String errorHook(String msg, int level) {
		if (globals == null ) return msg;
		final LuaThread r = globals.running;
		if (r.errorfunc == null)
			return globals.debuglib != null?
					msg + "\n" + globals.debuglib.traceback(level):
					msg;
		final LuaValue e = r.errorfunc;
		r.errorfunc = null;
		try {
			return e.call( LuaValue.valueOf(msg) ).tojstring();
		} catch ( Throwable t ) {
			return "error in error handling";
		} finally {
			r.errorfunc = e;
		}
	}

	private void processErrorHooks(LuaError le, Prototype p, int pc) {
		String file = "?";
		int line = -1;
		{
			CallFrame frame = null;
			if (globals != null && globals.debuglib != null) {
				frame = globals.debuglib.getCallFrame(le.level);
				if (frame != null) {
					String src = frame.shortsource();
					file = src != null ? src : "?";
					line = frame.currentline();
				}
			}
			if (frame == null) {
				file = p.source != null? p.source.tojstring(): "?";
				line = p.lineinfo != null && pc >= 0 && pc < p.lineinfo.length ? p.lineinfo[pc] : -1;
			}
		}
		
		String opcodeInfo = "";
		try {
			if (pc >= 0 && pc < p.code.length) {
				int instruction = p.code[pc];
				int opcode = instruction & 0x3f;
				int a = (instruction >> 6) & 0xff;
				String opName = opcode < Print.OPNAMES.length ? Print.OPNAMES[opcode] : "UNKNOWN";
				opcodeInfo = "\n  [指令] " + opName + " (opcode=" + opcode + ", A=" + a + ", PC=" + pc + ")";
				
				if (p.k != null && a < p.k.length && p.k[a] != null) {
					String constVal = p.k[a].tojstring();
					if (constVal.length() > 50) constVal = constVal.substring(0, 50) + "...";
					opcodeInfo += "\n  [常量 R(" + a + ")] " + constVal;
				}
			}
		} catch (Exception e) {
		}
		
		le.fileline = file + ":" + line;
		String originalMsg = le.getMessage();
		if (originalMsg != null && !originalMsg.contains("[指令]")) {
			try {
				LuaValue msgValue = LuaValue.valueOf(originalMsg + opcodeInfo);
				le = new LuaError(msgValue.tojstring());
				le.fileline = file + ":" + line;
			} catch (Exception e) {
			}
		}
		le.traceback = errorHook(le.getMessage(), le.level);
	}
	
	private UpValue findupval(LuaValue[] stack, short idx, UpValue[] openups) {
		final int n = openups.length;
		for (int i = 0; i < n; ++i)
			if (openups[i] != null && openups[i].index == idx)
				return openups[i];
		for (int i = 0; i < n; ++i)
			if (openups[i] == null)
				return openups[i] = new UpValue(stack, idx);
		error("No space for upvalue");
		return null;
	}

	protected LuaValue getUpvalue(int i) {
		return upValues[i].getValue();
	}
	
	protected void setUpvalue(int i, LuaValue v) {
		upValues[i].setValue(v);
	}

	public String name() {
		return "<"+p.shortsource()+":"+p.linedefined+">";
	}
	
	
}
