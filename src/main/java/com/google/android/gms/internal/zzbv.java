package com.google.android.gms.internal;

import com.google.android.gms.internal.zzaf.zza;
import java.lang.reflect.InvocationTargetException;

public class zzbv extends zzby {
    public zzbv(zzbc com_google_android_gms_internal_zzbc, String str, String str2, zza com_google_android_gms_internal_zzaf_zza, int i, int i2) {
        super(com_google_android_gms_internal_zzbc, str, str2, com_google_android_gms_internal_zzaf_zza, i, i2);
    }

    protected void zzbe() throws IllegalAccessException, InvocationTargetException {
        this.zzra.zzbJ = Integer.valueOf(2);
        boolean booleanValue = ((Boolean) this.zzrj.invoke(null, new Object[]{this.zzpC.getApplicationContext()})).booleanValue();
        synchronized (this.zzra) {
            if (booleanValue) {
                this.zzra.zzbJ = Integer.valueOf(1);
            } else {
                this.zzra.zzbJ = Integer.valueOf(0);
            }
        }
    }
}
