package com.tencent.bugly.crashreport.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import com.kayac.lobi.libnakamap.utils.NotificationUtil;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.j;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.s;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.y;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: BUGLY */
public final class a {
    private Context a;
    private long b;
    private int c;
    private boolean d = true;

    /* compiled from: BUGLY */
    class a implements Runnable {
        private boolean a;
        private UserInfoBean b;
        private /* synthetic */ a c;

        public a(a aVar, UserInfoBean userInfoBean, boolean z) {
            this.c = aVar;
            this.b = userInfoBean;
            this.a = z;
        }

        public final void run() {
            try {
                if (this.b != null) {
                    UserInfoBean userInfoBean = this.b;
                    if (userInfoBean != null) {
                        com.tencent.bugly.crashreport.common.info.a b = com.tencent.bugly.crashreport.common.info.a.b();
                        if (b != null) {
                            userInfoBean.j = b.e();
                        }
                    }
                    w.c("[UserInfo] Record user info.", new Object[0]);
                    a.a(this.c, this.b, false);
                }
                if (this.a) {
                    a aVar = this.c;
                    v a = v.a();
                    if (a != null) {
                        a.a(new Runnable(aVar) {
                            private /* synthetic */ a a;

                            {
                                this.a = r1;
                            }

                            public final void run() {
                                try {
                                    this.a.c();
                                } catch (Throwable th) {
                                    w.a(th);
                                }
                            }
                        });
                    }
                }
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* compiled from: BUGLY */
    class b implements Runnable {
        private /* synthetic */ a a;

        b(a aVar) {
            this.a = aVar;
        }

        public final void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < this.a.b) {
                v.a().a(new b(this.a), (this.a.b - currentTimeMillis) + 5000);
                return;
            }
            this.a.a(3, false, 0);
            this.a.a();
        }
    }

    /* compiled from: BUGLY */
    class c implements Runnable {
        private long a = 21600000;
        private /* synthetic */ a b;

        public c(a aVar, long j) {
            this.b = aVar;
            this.a = j;
        }

        public final void run() {
            a aVar = this.b;
            v a = v.a();
            if (a != null) {
                a.a(/* anonymous class already generated */);
            }
            aVar = this.b;
            long j = this.a;
            v.a().a(new c(aVar, j), j);
        }
    }

    static /* synthetic */ void a(a aVar, UserInfoBean userInfoBean, boolean z) {
        if (userInfoBean != null) {
            if (!(z || userInfoBean.b == 1)) {
                List a = aVar.a(com.tencent.bugly.crashreport.common.info.a.a(aVar.a).d);
                if (a != null && a.size() >= 20) {
                    w.a("[UserInfo] There are too many user info in local: %d", Integer.valueOf(a.size()));
                    return;
                }
            }
            long a2 = o.a().a("t_ui", a(userInfoBean), null, true);
            if (a2 >= 0) {
                w.c("[Database] insert %s success with ID: %d", "t_ui", Long.valueOf(a2));
                userInfoBean.a = a2;
            }
        }
    }

    public a(Context context, boolean z) {
        this.a = context;
        this.d = z;
    }

    public final void a(int i, boolean z, long j) {
        int i2 = 1;
        com.tencent.bugly.crashreport.common.strategy.a a = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (a == null || a.c().h || i == 1 || i == 3) {
            if (i == 1 || i == 3) {
                this.c++;
            }
            com.tencent.bugly.crashreport.common.info.a a2 = com.tencent.bugly.crashreport.common.info.a.a(this.a);
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.b = i;
            userInfoBean.c = a2.d;
            userInfoBean.d = a2.g();
            userInfoBean.e = System.currentTimeMillis();
            userInfoBean.f = -1;
            userInfoBean.n = a2.j;
            if (i != 1) {
                i2 = 0;
            }
            userInfoBean.o = i2;
            userInfoBean.l = a2.a();
            userInfoBean.m = a2.p;
            userInfoBean.g = a2.q;
            userInfoBean.h = a2.r;
            userInfoBean.i = a2.s;
            userInfoBean.k = a2.t;
            userInfoBean.r = a2.z();
            userInfoBean.s = a2.E();
            userInfoBean.p = a2.F();
            userInfoBean.q = a2.G();
            v.a().a(new a(this, userInfoBean, z), 0);
            return;
        }
        w.e("UserInfo is disable", new Object[0]);
    }

    public final void a() {
        this.b = y.b() + NotificationUtil.PROMOTION_NOTIFICATION_INTERVAL;
        v.a().a(new b(this), (this.b - System.currentTimeMillis()) + 5000);
    }

    private synchronized void c() {
        boolean z = false;
        synchronized (this) {
            if (this.d) {
                t a = t.a();
                if (a != null) {
                    com.tencent.bugly.crashreport.common.strategy.a a2 = com.tencent.bugly.crashreport.common.strategy.a.a();
                    if (a2 != null && (!a2.b() || a.b(1001))) {
                        boolean z2;
                        List list;
                        String str = com.tencent.bugly.crashreport.common.info.a.a(this.a).d;
                        List arrayList = new ArrayList();
                        List a3 = a(str);
                        if (a3 != null) {
                            int i;
                            UserInfoBean userInfoBean;
                            int i2;
                            int size = a3.size() - 20;
                            if (size > 0) {
                                for (int i3 = 0; i3 < a3.size() - 1; i3++) {
                                    for (i = i3 + 1; i < a3.size(); i++) {
                                        if (((UserInfoBean) a3.get(i3)).e > ((UserInfoBean) a3.get(i)).e) {
                                            userInfoBean = (UserInfoBean) a3.get(i3);
                                            a3.set(i3, a3.get(i));
                                            a3.set(i, userInfoBean);
                                        }
                                    }
                                }
                                for (i2 = 0; i2 < size; i2++) {
                                    arrayList.add(a3.get(i2));
                                }
                            }
                            Iterator it = a3.iterator();
                            i = 0;
                            while (it.hasNext()) {
                                userInfoBean = (UserInfoBean) it.next();
                                if (userInfoBean.f != -1) {
                                    it.remove();
                                    if (userInfoBean.e < y.b()) {
                                        arrayList.add(userInfoBean);
                                    }
                                }
                                if (userInfoBean.e <= System.currentTimeMillis() - 600000 || !(userInfoBean.b == 1 || userInfoBean.b == 4 || userInfoBean.b == 3)) {
                                    i2 = i;
                                } else {
                                    i2 = i + 1;
                                }
                                i = i2;
                            }
                            if (i > 15) {
                                w.d("[UserInfo] Upload user info too many times in 10 min: %d", Integer.valueOf(i));
                                z2 = false;
                            } else {
                                z2 = true;
                            }
                            list = a3;
                        } else {
                            Object arrayList2 = new ArrayList();
                            z2 = true;
                        }
                        if (arrayList.size() > 0) {
                            a(arrayList);
                        }
                        if (!z2 || list.size() == 0) {
                            w.c("[UserInfo] There is no user info in local database.", new Object[0]);
                        } else {
                            w.c("[UserInfo] Upload user info(size: %d)", Integer.valueOf(list.size()));
                            j a4 = com.tencent.bugly.proguard.a.a(list, this.c == 1 ? 1 : 2);
                            if (a4 == null) {
                                w.d("[UserInfo] Failed to create UserInfoPackage.", new Object[0]);
                            } else {
                                byte[] a5 = com.tencent.bugly.proguard.a.a(a4);
                                if (a5 == null) {
                                    w.d("[UserInfo] Failed to encode data.", new Object[0]);
                                } else {
                                    al a6 = com.tencent.bugly.proguard.a.a(this.a, a.a ? 840 : 640, a5);
                                    if (a6 == null) {
                                        w.d("[UserInfo] Request package is null.", new Object[0]);
                                    } else {
                                        s anonymousClass1 = new s(this) {
                                            private /* synthetic */ a b;

                                            public final void a(boolean z) {
                                                if (z) {
                                                    w.c("[UserInfo] Successfully uploaded user info.", new Object[0]);
                                                    long currentTimeMillis = System.currentTimeMillis();
                                                    for (UserInfoBean userInfoBean : list) {
                                                        userInfoBean.f = currentTimeMillis;
                                                        a.a(this.b, userInfoBean, true);
                                                    }
                                                }
                                            }
                                        };
                                        StrategyBean c = com.tencent.bugly.crashreport.common.strategy.a.a().c();
                                        String str2 = a.a ? c.r : c.t;
                                        String str3 = a.a ? StrategyBean.b : StrategyBean.a;
                                        t a7 = t.a();
                                        if (this.c == 1) {
                                            z = true;
                                        }
                                        a7.a(1001, a6, str2, str3, anonymousClass1, z);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public final void b() {
        v a = v.a();
        if (a != null) {
            a.a(/* anonymous class already generated */);
        }
    }

    public final List<UserInfoBean> a(String str) {
        Throwable th;
        Cursor cursor;
        Cursor a;
        try {
            a = o.a().a("t_ui", null, y.a(str) ? null : "_pc = '" + str + "'", null, null, true);
            if (a == null) {
                if (a != null) {
                    a.close();
                }
                return null;
            }
            try {
                StringBuilder stringBuilder = new StringBuilder();
                List<UserInfoBean> arrayList = new ArrayList();
                while (a.moveToNext()) {
                    UserInfoBean a2 = a(a);
                    if (a2 != null) {
                        arrayList.add(a2);
                    } else {
                        try {
                            stringBuilder.append(" or _id").append(" = ").append(a.getLong(a.getColumnIndex("_id")));
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                }
                String stringBuilder2 = stringBuilder.toString();
                if (stringBuilder2.length() > 0) {
                    int a3 = o.a().a("t_ui", stringBuilder2.substring(4), null, null, true);
                    w.d("[Database] deleted %s error data %d", "t_ui", Integer.valueOf(a3));
                }
                if (a != null) {
                    a.close();
                }
                return arrayList;
            } catch (Throwable th22) {
                th = th22;
            }
        } catch (Throwable th3) {
            th = th3;
            a = null;
            if (a != null) {
                a.close();
            }
            throw th;
        }
    }

    private static void a(List<UserInfoBean> list) {
        if (list != null && list.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            while (i < list.size() && i < 50) {
                stringBuilder.append(" or _id").append(" = ").append(((UserInfoBean) list.get(i)).a);
                i++;
            }
            String stringBuilder2 = stringBuilder.toString();
            if (stringBuilder2.length() > 0) {
                stringBuilder2 = stringBuilder2.substring(4);
            }
            stringBuilder.setLength(0);
            try {
                int a = o.a().a("t_ui", stringBuilder2, null, null, true);
                w.c("[Database] deleted %s data %d", "t_ui", Integer.valueOf(a));
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    private static ContentValues a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (userInfoBean.a > 0) {
                contentValues.put("_id", Long.valueOf(userInfoBean.a));
            }
            contentValues.put("_tm", Long.valueOf(userInfoBean.e));
            contentValues.put("_ut", Long.valueOf(userInfoBean.f));
            contentValues.put("_tp", Integer.valueOf(userInfoBean.b));
            contentValues.put("_pc", userInfoBean.c);
            contentValues.put("_dt", y.a((Parcelable) userInfoBean));
            return contentValues;
        } catch (Throwable th) {
            if (w.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    private static UserInfoBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            UserInfoBean userInfoBean = (UserInfoBean) y.a(blob, UserInfoBean.CREATOR);
            if (userInfoBean == null) {
                return userInfoBean;
            }
            userInfoBean.a = j;
            return userInfoBean;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
