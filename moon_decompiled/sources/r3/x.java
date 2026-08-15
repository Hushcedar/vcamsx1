package r3;

import androidx.core.os.perationCompat;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class x implements v3.f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1452a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final ArrayList f1453b;

    static {
        perationCompat.init0(729);
    }

    public x() {
        this.f1452a = 0;
        this.f1453b = new ArrayList();
    }

    public x(JSONObject jSONObject) {
        this();
        this.f1452a = jSONObject.optInt("firstAvailableUid");
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("appIds");
        if (jSONArrayOptJSONArray != null) {
            for (int i4 = 0; i4 < jSONArrayOptJSONArray.length(); i4++) {
                this.f1453b.add(Integer.valueOf(jSONArrayOptJSONArray.optInt(i4)));
            }
        }
    }

    @Override // v3.f
    public final native void a(JSONObject jSONObject);
}
