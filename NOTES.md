commit 6d2fb15c9c676547bb9bc05eeb8ce517f1f04c0a
Author: Prseniakov Arsenii <presnyakov04@inbox.ru>
Date:   Fri Feb 16 15:45:07 2024 +0300

    fixed mb
==================================================
Compiling 1 Java sources
Tests: running
WARNING: A command line option has enabled the Security Manager
WARNING: The Security Manager is deprecated and will be removed in a future release
Running class info.kgeorgiy.java.advanced.walk.WalkTest for info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk
=== Running test10_oneEmptyFile
    test10_oneEmptyFile finished in 63ms
=== Running test15_tenEmptyFiles
    test15_tenEmptyFiles finished in 30ms
=== Running test20_smallRandomFiles
    test20_smallRandomFiles finished in 56ms
=== Running test21_mediumRandomFiles
    test21_mediumRandomFiles finished in 80ms
=== Running test22_largeRandomFiles
    test22_largeRandomFiles finished in 279ms
=== Running test23_veryLargeFile
    test23_veryLargeFile finished in 126ms
=== Running test30_missingFiles
FileNotFoundException in readAndHash: HeJ38jcWLtoAqyyRRku7AyiGcE49to (Не удается найти указанный файл)FileNotFoundException in readAndHash: dNPDI6YVvM4qSRZ2k3DhL3AM1mYkjw (Не удается найти указанный файл)FileNotFoundException in readAndHash: XtS1ggWJgrCFraHEFHCvCG8IMryUIY (Не удается найти указанный файл)    test30_missingFiles finished in 29ms
=== Running test31_invalidFiles
    test31_invalidFiles finished in 9ms
=== Running test40_errorReading
FileNotFoundException in readAndHash: __Test__Walk__.. (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__@ (Не удается найти указанный файл)    test40_errorReading finished in 14ms
=== Running test45_partiallyMissingFiles
FileNotFoundException in readAndHash: no-such-file-3 (Не удается найти указанный файл)FileNotFoundException in readAndHash: no-such-file-2 (Не удается найти указанный файл)FileNotFoundException in readAndHash: no-such-file-1 (Не удается найти указанный файл)    test45_partiallyMissingFiles finished in 124ms
=== Running test46_filesAndDirs
FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\EKq1N4ov7kfNyLKyD7zlIRW61hlxZ9 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\XBVzaLmdso8VXNmVAyphO9Nrvbi4VZ (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\UfGgv27AU8s12VTaJErCOVoyJxz7jV (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\Q54DUuzkQ9JwBbPUbCxR7Li5ZuIh9J (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\I4dXlbNbnMFLtDNmttHxL80wirLq35 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\SzAEl9d0p6VaOSGSwq4h956NQuKG34 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\lBcHRIIFulG2raoD8zO6dsk7sZZ7u1 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\3Eqvb4wmSOpxoEPqobBnimjoFf2z17 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\zVuQT3kBrnb5WB0jGRjdo6uB5KNYtt (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test46_filesAndDirs\4UvrUm1QBL0GuGF0ICL15Fx7qyTLQh (Отказано в доступе)    test46_filesAndDirs finished in 54ms
=== Running test50_whitespaceSupport
    test50_whitespaceSupport finished in 16ms
=== Running test51_dirSupport
    test51_dirSupport finished in 39ms
=== Running test52_dirsSupport
    test52_dirsSupport finished in 44ms
=== Running test53_dirsHash
FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\eGgOTGynulB8B47Z8LJt5wOzhvJsOx (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\9zPC64069Oq94ukd4S3z75GlHelMk6 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\XNjvoxnfH4fJ0PYTFOg8t0XQqQRmh0 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\fH8DDDyTCHeJ38jcWLtoAqyyRRku7A (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\3AM1mYkjwXtS1ggWJgrCFraHEFHCvC (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\G8IMryUIY69knzGGyU4Cd5a8Sd1jrz (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\bYSwhK9zdUUakLzZnCQNlTcH2l5kSb (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\EVLDP0yNcCb2oHofIs6PjEQDXvlidd (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\vsZNPxeXizaY0ON8Kzp470NvinE997 (Отказано в доступе)FileNotFoundException in readAndHash: __Test__Walk__\test53_dirsHash\yiGcE49todNPDI6YVvM4qSRZ2k3DhL (Отказано в доступе)    test53_dirsHash finished in 13ms
=== Running test55_chineseSupport
    test55_chineseSupport finished in 34ms
=== Running test56_emojiSupport
    test56_emojiSupport finished in 53ms
=== Running test60_noInput
NullPointerException in main: Cannot invoke "java.io.File.mkdirs()" because the return value of "java.io.File.getParentFile()" is null    test60_noInput finished in 1ms
=== Running test61_invalidInput
NullPointerException in main: Cannot invoke "java.io.File.mkdirs()" because the return value of "java.io.File.getParentFile()" is nullNullPointerException in main: Cannot invoke "java.io.File.mkdirs()" because the return value of "java.io.File.getParentFile()" is null    test61_invalidInput finished in 0ms
=== Running test62_invalidOutput
NullPointerException in main: Cannot invoke "java.io.File.mkdirs()" because the return value of "java.io.File.getParentFile()" is nullNullPointerException in main: Cannot invoke "java.io.File.mkdirs()" because the return value of "java.io.File.getParentFile()" is nullIOException in main: __Test__Walk__\test62_invalidOutput\EVLDP0yNcCb2oHofIs6PjEQDXvlidd (Системе не удается найти указанный путь)    test62_invalidOutput finished in 2ms
=== Running test63_invalidFiles
    test63_invalidFiles finished in 4ms
=== Running test70_singleArgument
Error! Args is invalid    test70_singleArgument finished in 1ms
=== Running test71_noArguments
Error! Args is invalid    test71_noArguments finished in 1ms
=== Running test72_nullArguments
Error! Args is invalid    test72_nullArguments finished in 0ms
=== Running test73_firstArgumentNull
Error! Args is invalid    test73_firstArgumentNull finished in 0ms
=== Running test74_secondArgumentNull
Error! Args is invalid    test74_secondArgumentNull finished in 1ms
=== Running test75_threeArguments
Error! Args is invalid    test75_threeArguments finished in 2ms
Test test31_invalidFiles failed: Error thrown
java.lang.AssertionError: Error thrown
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.run(WalkUtil.java:243)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.check(WalkUtil.java:201)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.check(WalkUtil.java:191)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.check(WalkTest.java:240)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest$TestCase.test(WalkTest.java:270)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest$TestCase.test(WalkTest.java:259)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test31_invalidFiles(WalkTest.java:89)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
	at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
	at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
	at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
	at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.Tester.main(Tester.java:29)
Caused by: java.security.AccessControlException: access denied ("java.io.FilePermission" "\\.." "read")
	at java.base/java.security.AccessControlContext.checkPermission(AccessControlContext.java:488)
	at java.base/java.security.AccessController.checkPermission(AccessController.java:1071)
	at java.base/java.lang.SecurityManager.checkPermission(SecurityManager.java:411)
	at java.base/java.lang.SecurityManager.checkRead(SecurityManager.java:742)
	at java.base/java.io.FileInputStream.<init>(FileInputStream.java:141)
	at info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk.readAndGetHash(Walk.java:17)
	at info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk.main(Walk.java:72)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.run(WalkUtil.java:239)
	... 40 more
Test test50_whitespaceSupport failed: Error thrown
java.lang.AssertionError: Error thrown
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.run(WalkUtil.java:243)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.check(WalkUtil.java:201)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.check(WalkUtil.java:191)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.check(WalkTest.java:240)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest$TestCase.test(WalkTest.java:270)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest$TestCase.test(WalkTest.java:259)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.testRandomFiles(WalkTest.java:207)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.testAlphabet(WalkTest.java:217)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test50_whitespaceSupport(WalkTest.java:123)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
	at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
	at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
	at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
	at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.Tester.main(Tester.java:29)
Caused by: java.security.AccessControlException: access denied ("java.io.FilePermission" "__Test__Walk__\test50_whitespaceSupport\  __  __ _ _    _ __   ___    " "read")
	at java.base/java.security.AccessControlContext.checkPermission(AccessControlContext.java:488)
	at java.base/java.security.AccessController.checkPermission(AccessController.java:1071)
	at java.base/java.lang.SecurityManager.checkPermission(SecurityManager.java:411)
	at java.base/java.lang.SecurityManager.checkRead(SecurityManager.java:742)
	at java.base/java.io.FileInputStream.<init>(FileInputStream.java:141)
	at info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk.readAndGetHash(Walk.java:17)
	at info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk.main(Walk.java:72)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.run(WalkUtil.java:239)
	... 42 more
Test test63_invalidFiles failed: Error thrown
java.lang.AssertionError: Error thrown
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.run(WalkUtil.java:243)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.check(WalkUtil.java:201)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.check(WalkUtil.java:191)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.check(WalkTest.java:240)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest$TestCase.test(WalkTest.java:270)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest$TestCase.test(WalkTest.java:259)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.testRandomFiles(WalkTest.java:207)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.testAlphabet(WalkTest.java:217)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test63_invalidFiles(WalkTest.java:173)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
	at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
	at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
	at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
	at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.Tester.main(Tester.java:29)
Caused by: java.security.AccessControlException: access denied ("java.io.FilePermission" "__Test__Walk__\test63_invalidFiles\ **\***\  \*\***\*   *\ \ " "read")
	at java.base/java.security.AccessControlContext.checkPermission(AccessControlContext.java:488)
	at java.base/java.security.AccessController.checkPermission(AccessController.java:1071)
	at java.base/java.lang.SecurityManager.checkPermission(SecurityManager.java:411)
	at java.base/java.lang.SecurityManager.checkRead(SecurityManager.java:742)
	at java.base/java.io.FileInputStream.<init>(FileInputStream.java:141)
	at info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk.readAndGetHash(Walk.java:17)
	at info.kgeorgiy.ja.Presniakov_Arsenii.walk.Walk.main(Walk.java:72)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkUtil.run(WalkUtil.java:239)
	... 42 more
ERROR: Tests: failed
