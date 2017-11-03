package hlc.base.annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Logs {
	String name()default "execute";
	String fId() default "";
	String mId() default "";
}
