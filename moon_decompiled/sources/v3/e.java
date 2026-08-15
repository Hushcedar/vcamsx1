package v3;

import android.util.SparseArray;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final /* synthetic */ int f1728a = 0;

    public static class a extends JSONArray {
        @Override // org.json.JSONArray
        public final JSONArray put(double d4) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(int i4) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(int i4, double d4) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(int i4, int i5) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(int i4, long j4) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(int i4, Object obj) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(int i4, boolean z3) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(long j4) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(Object obj) {
            return this;
        }

        @Override // org.json.JSONArray
        public final JSONArray put(boolean z3) {
            return this;
        }
    }

    static {
        new a();
    }

    public static JSONArray a(SparseArray<? extends f> sparseArray) {
        JSONArray jSONArray = new JSONArray();
        for (int i4 = 0; i4 < sparseArray.size(); i4++) {
            try {
                JSONObject jSONObject = new JSONObject();
                sparseArray.valueAt(i4).a(jSONObject);
                jSONArray.put(jSONObject);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        return jSONArray;
    }

    public static JSONObject b(Map<String, ? extends f> map) {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, ? extends f> entry : map.entrySet()) {
            JSONObject jSONObject2 = new JSONObject();
            try {
                entry.getValue().a(jSONObject2);
                jSONObject.put(entry.getKey(), jSONObject2);
            } catch (JSONException e4) {
                e4.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static LinkedHashMap c(JSONObject jSONObject) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (jSONObject == null) {
            return linkedHashMap;
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            linkedHashMap.put(next, jSONObject.opt(next));
        }
        return linkedHashMap;
    }

    public static String[] d(JSONArray jSONArray) {
        if (jSONArray == null) {
            return null;
        }
        String[] strArr = new String[jSONArray.length()];
        for (int i4 = 0; i4 < jSONArray.length(); i4++) {
            strArr[i4] = jSONArray.optString(i4);
        }
        return strArr;
    }
}
