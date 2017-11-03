package hlc.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public  @interface Properties {
	boolean IsPk() default false;
	String enName() default "";
	String cnName() default "";
	String dataType() default "String";
	boolean needSave() default true;
}
