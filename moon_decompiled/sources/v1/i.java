package v1;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
@Retention(RetentionPolicy.RUNTIME)
public @interface i {
    Class[] classes() default {};

    String[] strings() default {};

    int type() default 0;
}
