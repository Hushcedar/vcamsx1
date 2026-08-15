package v3;

import android.os.Parcel;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class g extends h implements f {
    public g(File file) {
        super(file);
    }

    public abstract void a(JSONObject jSONObject);

    @Override // v3.h
    public final void e(Parcel parcel, int i4) {
        try {
            int i5 = parcel.readInt();
            byte[] bArr = new byte[i5];
            parcel.readByteArray(bArr);
            for (int i6 = 0; i6 < i5; i6++) {
                bArr[i6] = (byte) (bArr[i6] ^ 3194657);
            }
            i(new JSONObject(new String(bArr)));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // v3.h
    public final void g(Parcel parcel) {
        JSONObject jSONObject = new JSONObject();
        try {
            a(jSONObject);
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
        byte[] bytes = jSONObject.toString().getBytes();
        for (int i4 = 0; i4 < bytes.length; i4++) {
            bytes[i4] = (byte) (bytes[i4] ^ 3194657);
        }
        parcel.writeInt(bytes.length);
        parcel.writeByteArray(bytes);
    }

    public abstract void i(JSONObject jSONObject);
}
