package cn.sharesdk.framework;

import cn.sharesdk.framework.utils.d;

class g extends Thread {
    final /* synthetic */ int a;
    final /* synthetic */ Object b;
    final /* synthetic */ f c;

    g(f fVar, int i, Object obj) {
        this.c = fVar;
        this.a = i;
        this.b = obj;
    }

    public void run() {
        try {
            this.c.j();
            if (this.c.a.checkAuthorize(this.a, this.b)) {
                this.c.b(this.a, this.b);
            }
        } catch (Throwable th) {
            d.a().w(th);
        }
    }
}
