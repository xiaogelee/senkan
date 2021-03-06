package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import android.os.Process;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
public final class e implements UncaughtExceptionHandler {
    private static String h = null;
    private static final Object i = new Object();
    private Context a;
    private b b;
    private a c;
    private com.tencent.bugly.crashreport.common.info.a d;
    private UncaughtExceptionHandler e;
    private UncaughtExceptionHandler f;
    private boolean g = false;
    private int j;

    public e(Context context, b bVar, a aVar, com.tencent.bugly.crashreport.common.info.a aVar2) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    public final synchronized void a() {
        if (this.j >= 10) {
            w.a("java crash handler over %d, no need set.", Integer.valueOf(10));
        } else {
            UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (!(defaultUncaughtExceptionHandler == null || getClass().getName().equals(defaultUncaughtExceptionHandler.getClass().getName()))) {
                if ("com.android.internal.os.RuntimeInit$UncaughtHandler".equals(defaultUncaughtExceptionHandler.getClass().getName())) {
                    w.a("backup system java handler: %s", defaultUncaughtExceptionHandler.toString());
                    this.f = defaultUncaughtExceptionHandler;
                    this.e = defaultUncaughtExceptionHandler;
                } else {
                    w.a("backup java handler: %s", defaultUncaughtExceptionHandler.toString());
                    this.e = defaultUncaughtExceptionHandler;
                }
                a(defaultUncaughtExceptionHandler);
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.g = true;
                this.j++;
                w.a("registered java monitor: %s", toString());
            }
        }
    }

    public final synchronized void b() {
        this.g = false;
        w.a("close java monitor!", new Object[0]);
        if (Thread.getDefaultUncaughtExceptionHandler().getClass().getName().contains("bugly")) {
            w.a("Java monitor to unregister: %s", toString());
            Thread.setDefaultUncaughtExceptionHandler(this.e);
            this.j--;
        }
    }

    private synchronized void a(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.e = uncaughtExceptionHandler;
    }

    private CrashDetailBean b(Thread thread, Throwable th, boolean z, String str, byte[] bArr) {
        if (th == null) {
            w.d("We can do nothing with a null throwable.", new Object[0]);
            return null;
        }
        Object a;
        boolean l = c.a().l();
        String str2 = (l && z) ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        if (l && z) {
            w.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.B = b.g();
        crashDetailBean.C = b.e();
        crashDetailBean.D = b.i();
        crashDetailBean.E = this.d.p();
        crashDetailBean.F = this.d.o();
        crashDetailBean.G = this.d.q();
        crashDetailBean.w = y.a(this.a, c.d, null);
        crashDetailBean.x = x.a(z);
        String str3 = "user log size:%d";
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(crashDetailBean.x == null ? 0 : crashDetailBean.x.length);
        w.a(str3, objArr);
        crashDetailBean.b = z ? 0 : 2;
        crashDetailBean.e = this.d.h();
        crashDetailBean.f = this.d.j;
        crashDetailBean.g = this.d.w();
        crashDetailBean.m = this.d.g();
        String name = th.getClass().getName();
        String b = b(th, 1000);
        if (b == null) {
            b = "";
        }
        String str4 = "stack frame :%d, has cause %b";
        Object[] objArr2 = new Object[2];
        objArr2[0] = Integer.valueOf(th.getStackTrace().length);
        objArr2[1] = Boolean.valueOf(th.getCause() != null);
        w.e(str4, objArr2);
        str3 = "";
        if (th.getStackTrace().length > 0) {
            str3 = th.getStackTrace()[0].toString();
        }
        Throwable th2 = th;
        while (th2 != null && th2.getCause() != null) {
            th2 = th2.getCause();
        }
        if (th2 == null || th2 == th) {
            crashDetailBean.n = name;
            crashDetailBean.o = b + str2;
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = str3;
            a = a(th, c.e);
            crashDetailBean.q = a;
        } else {
            crashDetailBean.n = th2.getClass().getName();
            crashDetailBean.o = b(th2, 1000);
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = th2.getStackTrace()[0].toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name).append(":").append(b).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append(str3);
            stringBuilder.append("\n......");
            stringBuilder.append("\nCaused by:\n");
            stringBuilder.append(crashDetailBean.n).append(":").append(crashDetailBean.o).append(IOUtils.LINE_SEPARATOR_UNIX);
            a = a(th2, c.e);
            stringBuilder.append(a);
            crashDetailBean.q = stringBuilder.toString();
        }
        crashDetailBean.r = System.currentTimeMillis();
        crashDetailBean.u = y.b(crashDetailBean.q.getBytes());
        try {
            crashDetailBean.y = y.a(c.e, false);
            crashDetailBean.z = this.d.d;
            crashDetailBean.A = thread.getName() + "(" + thread.getId() + ")";
            crashDetailBean.y.put(crashDetailBean.A, a);
            crashDetailBean.H = this.d.y();
            crashDetailBean.h = this.d.v();
            crashDetailBean.i = this.d.I();
            crashDetailBean.L = this.d.a;
            crashDetailBean.M = this.d.a();
            crashDetailBean.O = this.d.F();
            crashDetailBean.P = this.d.G();
            crashDetailBean.Q = this.d.z();
            crashDetailBean.R = this.d.E();
        } catch (Throwable th3) {
            w.e("handle crash error %s", th3.toString());
        }
        if (z) {
            this.b.b(crashDetailBean);
        } else {
            Object obj = (str == null || str.length() <= 0) ? null : 1;
            a = (bArr == null || bArr.length <= 0) ? null : 1;
            if (obj != null) {
                crashDetailBean.N = new HashMap(1);
                crashDetailBean.N.put("UserData", str);
            }
            if (a != null) {
                crashDetailBean.S = bArr;
            }
        }
        return crashDetailBean;
    }

    private static boolean a(Thread thread) {
        boolean z;
        synchronized (i) {
            if (h == null || !thread.getName().equals(h)) {
                h = thread.getName();
                z = false;
            } else {
                z = true;
            }
        }
        return z;
    }

    public final void a(Thread thread, Throwable th, boolean z, String str, byte[] bArr) {
        if (z) {
            w.e("Java Crash Happen cause by %s(%d)", thread.getName(), Long.valueOf(thread.getId()));
            if (a(thread)) {
                w.a("this class has handled this exception", new Object[0]);
                if (this.f != null) {
                    w.a("call system handler", new Object[0]);
                    this.f.uncaughtException(thread, th);
                } else {
                    w.e("current process die", new Object[0]);
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                }
            }
        } else {
            w.e("Java Catch Happen", new Object[0]);
        }
        try {
            if (this.g) {
                if (!this.c.b()) {
                    w.e("waiting for remote sync", new Object[0]);
                    int i = 0;
                    while (!this.c.b()) {
                        y.b(500);
                        i += 500;
                        if (i >= 3000) {
                            break;
                        }
                    }
                }
                if (!this.c.b()) {
                    w.d("no remote but still store!", new Object[0]);
                }
                if (this.c.c().g || !this.c.b()) {
                    CrashDetailBean b = b(thread, th, z, str, bArr);
                    if (b == null) {
                        w.e("pkg crash datas fail!", new Object[0]);
                        if (!z) {
                            return;
                        }
                        if (this.e != null && b(this.e)) {
                            w.e("sys default last handle start!", new Object[0]);
                            this.e.uncaughtException(thread, th);
                            w.e("sys default last handle end!", new Object[0]);
                            return;
                        } else if (this.f != null) {
                            w.e("system handle start!", new Object[0]);
                            this.f.uncaughtException(thread, th);
                            w.e("system handle end!", new Object[0]);
                            return;
                        } else {
                            w.e("crashreport last handle start!", new Object[0]);
                            w.e("current process die", new Object[0]);
                            Process.killProcess(Process.myPid());
                            System.exit(1);
                            w.e("crashreport last handle end!", new Object[0]);
                            return;
                        }
                    }
                    b.a(z ? "JAVA_CRASH" : "JAVA_CATCH", y.a(), this.d.d, thread, y.a(th), b);
                    if (!this.b.a(b)) {
                        this.b.a(b, 3000, z);
                    }
                    if (!z) {
                        return;
                    }
                    if (this.e != null && b(this.e)) {
                        w.e("sys default last handle start!", new Object[0]);
                        this.e.uncaughtException(thread, th);
                        w.e("sys default last handle end!", new Object[0]);
                        return;
                    } else if (this.f != null) {
                        w.e("system handle start!", new Object[0]);
                        this.f.uncaughtException(thread, th);
                        w.e("system handle end!", new Object[0]);
                        return;
                    } else {
                        w.e("crashreport last handle start!", new Object[0]);
                        w.e("current process die", new Object[0]);
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                        w.e("crashreport last handle end!", new Object[0]);
                        return;
                    }
                }
                String str2;
                w.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
                if (z) {
                    str2 = "JAVA_CRASH";
                } else {
                    str2 = "JAVA_CATCH";
                }
                b.a(str2, y.a(), this.d.d, thread, y.a(th), null);
                if (!z) {
                    return;
                }
                if (this.e != null && b(this.e)) {
                    w.e("sys default last handle start!", new Object[0]);
                    this.e.uncaughtException(thread, th);
                    w.e("sys default last handle end!", new Object[0]);
                    return;
                } else if (this.f != null) {
                    w.e("system handle start!", new Object[0]);
                    this.f.uncaughtException(thread, th);
                    w.e("system handle end!", new Object[0]);
                    return;
                } else {
                    w.e("crashreport last handle start!", new Object[0]);
                    w.e("current process die", new Object[0]);
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                    w.e("crashreport last handle end!", new Object[0]);
                    return;
                }
            }
            w.c("Java crash handler is disable. Just return.", new Object[0]);
            if (!z) {
                return;
            }
            if (this.e != null && b(this.e)) {
                w.e("sys default last handle start!", new Object[0]);
                this.e.uncaughtException(thread, th);
                w.e("sys default last handle end!", new Object[0]);
            } else if (this.f != null) {
                w.e("system handle start!", new Object[0]);
                this.f.uncaughtException(thread, th);
                w.e("system handle end!", new Object[0]);
            } else {
                w.e("crashreport last handle start!", new Object[0]);
                w.e("current process die", new Object[0]);
                Process.killProcess(Process.myPid());
                System.exit(1);
                w.e("crashreport last handle end!", new Object[0]);
            }
        } catch (Throwable th2) {
            if (z) {
                if (this.e != null && b(this.e)) {
                    w.e("sys default last handle start!", new Object[0]);
                    this.e.uncaughtException(thread, th);
                    w.e("sys default last handle end!", new Object[0]);
                } else if (this.f != null) {
                    w.e("system handle start!", new Object[0]);
                    this.f.uncaughtException(thread, th);
                    w.e("system handle end!", new Object[0]);
                } else {
                    w.e("crashreport last handle start!", new Object[0]);
                    w.e("current process die", new Object[0]);
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                    w.e("crashreport last handle end!", new Object[0]);
                }
            }
        }
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        synchronized (i) {
            a(thread, th, true, null, null);
        }
    }

    private static boolean b(UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler == null) {
            return true;
        }
        String name = uncaughtExceptionHandler.getClass().getName();
        String str = "uncaughtException";
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            if (name.equals(className) && str.equals(methodName)) {
                return false;
            }
        }
        return true;
    }

    public final synchronized void a(StrategyBean strategyBean) {
        if (strategyBean != null) {
            if (strategyBean.g != this.g) {
                w.a("java changed to %b", Boolean.valueOf(strategyBean.g));
                if (strategyBean.g) {
                    a();
                } else {
                    b();
                }
            }
        }
    }

    private static String a(Throwable th, int i) {
        if (th == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (th.getStackTrace() != null) {
                StackTraceElement[] stackTrace = th.getStackTrace();
                int length = stackTrace.length;
                int i2 = 0;
                while (i2 < length) {
                    StackTraceElement stackTraceElement = stackTrace[i2];
                    if (i <= 0 || stringBuilder.length() < i) {
                        stringBuilder.append(stackTraceElement.toString()).append(IOUtils.LINE_SEPARATOR_UNIX);
                        i2++;
                    } else {
                        stringBuilder.append("\n[Stack over limit size :" + i + " , has been cutted !]");
                        return stringBuilder.toString();
                    }
                }
            }
        } catch (Throwable th2) {
            w.e("gen stack error %s", th2.toString());
        }
        return stringBuilder.toString();
    }

    private static String b(Throwable th, int i) {
        if (th.getMessage() == null) {
            return "";
        }
        if (th.getMessage().length() <= 1000) {
            return th.getMessage();
        }
        return th.getMessage().substring(0, 1000) + "\n[Message over limit size:1000" + ", has been cutted!]";
    }
}
