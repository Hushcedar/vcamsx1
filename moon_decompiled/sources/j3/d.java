package j3;

import androidx.core.os.perationCompat;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: classes.dex */
public final class d implements XmlSerializer {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final String[] f563k;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f565b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Writer f566c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public OutputStream f567d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public CharsetEncoder f568e;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public boolean f571h;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f570g = false;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f572i = 0;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public boolean f573j = true;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final char[] f564a = new char[32768];

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ByteBuffer f569f = ByteBuffer.allocate(32768);

    static {
        perationCompat.init0(175);
        f563k = new String[]{"&#0;", "&#1;", "&#2;", "&#3;", "&#4;", "&#5;", "&#6;", "&#7;", "&#8;", "&#9;", "&#10;", "&#11;", "&#12;", "&#13;", "&#14;", "&#15;", "&#16;", "&#17;", "&#18;", "&#19;", "&#20;", "&#21;", "&#22;", "&#23;", "&#24;", "&#25;", "&#26;", "&#27;", "&#28;", "&#29;", "&#30;", "&#31;", null, null, "&quot;", null, null, null, "&amp;", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&lt;", null, "&gt;", null};
    }

    public final native void a(char c4);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native XmlSerializer attribute(String str, String str2, String str3);

    public final native void b(int i4, String str, int i5);

    public final native void c(char[] cArr, int i4, int i5);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void cdsect(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void comment(String str);

    public final native void d(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void docdecl(String str);

    public final native void e();

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void endDocument();

    @Override // org.xmlpull.v1.XmlSerializer
    public final native XmlSerializer endTag(String str, String str2);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void entityRef(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void flush();

    @Override // org.xmlpull.v1.XmlSerializer
    public final native int getDepth();

    @Override // org.xmlpull.v1.XmlSerializer
    public final native boolean getFeature(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native String getName();

    @Override // org.xmlpull.v1.XmlSerializer
    public final native String getNamespace();

    @Override // org.xmlpull.v1.XmlSerializer
    public final native String getPrefix(String str, boolean z3);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native Object getProperty(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void ignorableWhitespace(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void processingInstruction(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void setFeature(String str, boolean z3);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void setOutput(OutputStream outputStream, String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void setOutput(Writer writer);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void setPrefix(String str, String str2);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void setProperty(String str, Object obj);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native void startDocument(String str, Boolean bool);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native XmlSerializer startTag(String str, String str2);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native XmlSerializer text(String str);

    @Override // org.xmlpull.v1.XmlSerializer
    public final native XmlSerializer text(char[] cArr, int i4, int i5);
}
