# Plan

1. **Enhance interactive input (autocompletion, better history)**:
   - We can't easily add JLine here, but we can add basic help matching.

2. **Improve table formatting (`truncate` & `padRight` & `printTable`)**:
   - The current `showVariables` limits table output. We could add a command to properly dump tables with depth formatting.

3. **Improve Local Variable Printing**:
   - The current `showVariables` prints local variables, but it prints variables based on `startpc` without properly respecting if the variable is currently alive. We need to check `currentPc` vs `startpc` and `endpc`.

4. **Enhanced evaluation**:
   - Evaluate expressions in the context of the current stack frame, allowing users to modify/read locals via `eval`! Wait, lua debugger eval only has access to globals currently. We can't easily evaluate locals unless we do bytecode magic, but we can at least allow easier access by updating `evalCode` to pass local variables if possible. Wait, easier: `getLocal(name)` and `setLocal(name, value)` commands or simply supporting local variable resolution.

5. **Let's modify `LuaDebugger.java`**:
   - Update `showVariables` to properly check `currentPc` to see if a local variable is alive.
   - Add a `dump` command for full table dumping (better formatting).
   - Improve `breakPoint` display to show current local variables.
   - Enhance the command loop help.

Let's refine what the user means by "magic changes, make it more intelligent, easier to debug lua scripts".
I will implement:
- **Smart variable resolution in eval**: If `eval` is used, maybe we can inject local variables into a temporary environment so `eval <expr>` can read local variables! This makes debugging MUCH easier.
- **Better stack frame inspection**: Allow `up` and `down` commands to traverse the call stack and inspect variables in different frames!
- **Table pretty printing**: A `print` command that formats a table as JSON/Lua.
- **Watchpoints**: The `watchList` is already there, let's make it evaluate expressions, not just global variables.

Let's list these steps in the plan.
